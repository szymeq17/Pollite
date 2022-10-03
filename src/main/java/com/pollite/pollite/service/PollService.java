package com.pollite.pollite.service;

import com.pollite.pollite.dto.PollAnswerResult;
import com.pollite.pollite.dto.PollDto;
import com.pollite.pollite.dto.PollResults;
import com.pollite.pollite.dto.mapper.PollAnswerMapper;
import com.pollite.pollite.dto.mapper.PollMapper;
import com.pollite.pollite.exception.PollAnswerDoesNotExistException;
import com.pollite.pollite.exception.PollDoesNotExistException;
import com.pollite.pollite.exception.UserDoesNotExistException;
import com.pollite.pollite.exception.UserNotAuthorizedException;
import com.pollite.pollite.model.PollAnswer;
import com.pollite.pollite.repository.PollAnswerRepository;
import com.pollite.pollite.repository.PollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PollService {
    private final PollRepository pollRepository;
    private final PollAnswerRepository pollAnswerRepository;
    private final UserService userService;

    private final PollMapper pollMapper;

    private final PollAnswerMapper pollAnswerMapper;

    public void addPoll(PollDto pollDTO, Principal principal) throws UserDoesNotExistException {
        var owner = userService.findUserByUsername(principal.getName());
        var poll = pollMapper.fromDto(pollDTO);
        poll.setOwner(owner);
        pollRepository.save(poll);
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

    @Transactional
    public void vote(Long pollAnswerId) throws PollAnswerDoesNotExistException {
        if (!pollAnswerRepository.existsById(pollAnswerId)) {
            throw new PollAnswerDoesNotExistException(pollAnswerId);
        }
        pollAnswerRepository.incrementVotes(pollAnswerId);
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

    private List<PollAnswer> createPollAnswers(List<String> answers) {
        return answers.stream()
                .map(answer -> PollAnswer.builder()
                        .text(answer)
                        .votesTotal(0L)
                        .build())
                .collect(Collectors.toList());
    }
}
