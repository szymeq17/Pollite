package com.pollite.pollite.service;

import com.pollite.pollite.dto.PollAnswerResult;
import com.pollite.pollite.dto.PollResults;
import com.pollite.pollite.dto.PollTemplate;
import com.pollite.pollite.exception.PollAnswerDoesNotExistException;
import com.pollite.pollite.exception.PollDoesNotExistException;
import com.pollite.pollite.exception.UserDoesNotExistException;
import com.pollite.pollite.model.Poll;
import com.pollite.pollite.model.PollAnswer;
import com.pollite.pollite.repository.PollAnswerRepository;
import com.pollite.pollite.repository.PollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void addPoll(PollTemplate pollTemplate, Principal principal) throws UserDoesNotExistException {
        var owner = userService.findUserByUsername(principal.getName());
        var poll = Poll.builder()
                .owner(owner)
                .text(pollTemplate.getText())
                .pollAnswers(createPollAnswers(pollTemplate.getAnswers()))
                .startDateTime(pollTemplate.getStartDateTime())
                .endDateTime(pollTemplate.getEndDateTime())
                .build();
        pollRepository.save(poll);
    }

    public void vote(Long pollAnswerId) throws PollAnswerDoesNotExistException, UserDoesNotExistException {
        var pollAnswer = pollAnswerRepository.findById(pollAnswerId)
                .orElseThrow(() -> new PollAnswerDoesNotExistException(pollAnswerId));

        pollAnswer.addVote();
        pollAnswerRepository.save(pollAnswer);
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
