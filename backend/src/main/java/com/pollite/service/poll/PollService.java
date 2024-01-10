package com.pollite.service.poll;

import com.pollite.dto.PollAnswerResult;
import com.pollite.dto.PollDto;
import com.pollite.dto.PollInfoDto;
import com.pollite.dto.PollResults;
import com.pollite.dto.mapper.PollMapper;
import com.pollite.exception.PollAnswerDoesNotExistException;
import com.pollite.exception.PollDoesNotExistException;
import com.pollite.exception.PollNotActiveException;
import com.pollite.exception.UserDoesNotExistException;
import com.pollite.exception.UserNotAuthorizedException;
import com.pollite.model.Poll;
import com.pollite.model.PollAnswer;
import com.pollite.repository.PollAnswerRepository;
import com.pollite.repository.PollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PollService {
    private final PollRepository pollRepository;
    private final PollAnswerRepository pollAnswerRepository;
    private final UserService userService;

    private final PollMapper pollMapper;

    private final Clock clock;

    public void addPoll(PollDto pollDTO, Principal principal) throws UserDoesNotExistException {
        var owner = userService.findUserByUsername(principal.getName());
        var poll = pollMapper.fromDto(pollDTO);
        poll.setOwner(owner);
        pollRepository.save(poll);
    }

    public Optional<PollDto> findPoll(Long id) {
        return pollRepository.findById(id).map(pollMapper::toDto);
    }

    public Page<PollInfoDto> findUsersPollInfos(String username, Pageable pageable, Principal principal) {
        if (!principal.getName().equals(username)) {
            throw new UserNotAuthorizedException(principal.getName());
        }

        return pollRepository.findAllByOwnerUsername(username, pageable).map(pollMapper::toPollInfoDto);
    }

    public Page<PollDto> getPolls(Pageable pageable) {
        return pollRepository.findAll(pageable).map(pollMapper::toDto);
    }

    public void deletePoll(Long pollId, Principal principal) throws UserDoesNotExistException, PollDoesNotExistException, UserNotAuthorizedException {
        var user = userService.findUserByUsername(principal.getName());
        var poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new PollDoesNotExistException(pollId));

        if (!poll.getOwner().equals(user)) {
            throw new UserNotAuthorizedException(user.getUsername());
        }

        pollRepository.deleteById(pollId);
    }

    public void editPoll(PollDto pollDto, Principal principal) throws UserDoesNotExistException, UserNotAuthorizedException {
        var user = userService.findUserByUsername(principal.getName());

        var poll = pollMapper.fromDto(pollDto);

        if (!poll.getOwner().equals(user)) {
            throw new UserNotAuthorizedException(user.getUsername());
        }

        pollRepository.save(poll);
    }

    @Transactional
    public PollResults vote(Long pollId, Long pollAnswerId)
            throws PollAnswerDoesNotExistException, PollNotActiveException, PollDoesNotExistException {
        var poll = pollRepository.findById(pollId).orElseThrow(() -> new PollDoesNotExistException(pollId));

        if (!isPollActive(poll)) {
            throw new PollNotActiveException(poll.getId());
        }

        if (!pollAnswerRepository.existsById(pollAnswerId)) {
            throw new PollAnswerDoesNotExistException(pollAnswerId);
        }

        pollAnswerRepository.incrementVotes(pollAnswerId);

        return getPollResults(pollId);
    }

    public PollResults getPollResults(Long pollId) throws PollDoesNotExistException {
        var poll = pollRepository.findById(pollId).orElseThrow(() -> new PollDoesNotExistException(pollId));
        var votesTotal = calculateVotesTotal(poll.getPollAnswers());

        return PollResults.builder()
                .results(buildPollAnswersResults(poll.getPollAnswers(), votesTotal))
                .votesTotal(votesTotal)
                .build();
    }

    private Long calculateVotesTotal(List<PollAnswer> pollAnswers) {
        return pollAnswers.stream()
                .map(PollAnswer::getVotesTotal)
                .reduce(0L, Long::sum);
    }

    private List<PollAnswerResult> buildPollAnswersResults(List<PollAnswer> pollAnswers, Long votesTotal) {
        return pollAnswers.stream()
                .map(pollAnswer -> PollAnswerResult.builder()
                        .pollAnswerText(pollAnswer.getText())
                        .votes(pollAnswer.getVotesTotal())
                        .percentage(calculatePercentage(pollAnswer.getVotesTotal(), votesTotal))
                        .build())
                .collect(Collectors.toList());
    }

    private BigDecimal calculatePercentage(Long pollAnswerVotesTotal, Long pollVotesTotal) {
        if (pollVotesTotal == 0L) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(pollAnswerVotesTotal)
                .divide(
                        BigDecimal.valueOf(pollVotesTotal),
                        new MathContext(4, RoundingMode.HALF_DOWN)
                )
                .scaleByPowerOfTen(2);
    }

    private boolean isPollActive(Poll poll) {
        var now = OffsetDateTime.now(clock);
        var pollStartDate = poll.getStartDateTime();
        var pollEndDate = poll.getEndDateTime();

        var isAfterStartDate = pollStartDate == null || now.compareTo(pollStartDate) >= 0;
        var isBeforeEndDate = pollEndDate == null || now.compareTo(pollEndDate) <= 0;

        return isAfterStartDate && isBeforeEndDate;
    }
}
