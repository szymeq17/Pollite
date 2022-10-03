package com.pollite.pollite.service;

import com.pollite.pollite.dto.PollAnswerDto;
import com.pollite.pollite.dto.PollDto;
import com.pollite.pollite.dto.mapper.PollAnswerMapper;
import com.pollite.pollite.dto.mapper.PollMapper;
import com.pollite.pollite.exception.PollAnswerDoesNotExistException;
import com.pollite.pollite.exception.PollDoesNotExistException;
import com.pollite.pollite.exception.UserDoesNotExistException;
import com.pollite.pollite.exception.UserNotAuthorizedException;
import com.pollite.pollite.model.Poll;
import com.pollite.pollite.model.PollAnswer;
import com.pollite.pollite.model.User;
import com.pollite.pollite.repository.PollAnswerRepository;
import com.pollite.pollite.repository.PollRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PollServiceTest {

    private static final String EXAMPLE_TEXT = "Example text";
    private static final List<String> EXAMPLE_ANSWERS = List.of("Answer 1", "Answer 3", "Answer 2");
    private static final String EXAMPLE_USERNAME = "user";

    @InjectMocks
    private PollService sut;

    @Mock
    private PollRepository pollRepository;

    @Mock
    private PollAnswerRepository pollAnswerRepository;

    @Mock
    private UserService userService;

    @Mock
    private PollMapper pollMapper;

    @Mock
    private Principal principal;

    @Captor
    private ArgumentCaptor<Poll> pollCaptor;

    @Test
    public void shouldAddPoll() throws Exception {
        //given
        var pollDTO  = PollDto.builder()
                .text(EXAMPLE_TEXT)
                .pollAnswers(createPollAnswerDtoList(EXAMPLE_ANSWERS))
                .build();

        var user = createUser(EXAMPLE_USERNAME);

        var poll = Poll.builder()
                .text(EXAMPLE_TEXT)
                .pollAnswers(createPollAnswerList(EXAMPLE_ANSWERS))
                .owner(user)
                .build();

        when(principal.getName()).thenReturn(EXAMPLE_USERNAME);
        when(pollMapper.fromDto(pollDTO)).thenReturn(poll);
        when(userService.findUserByUsername(EXAMPLE_USERNAME)).thenReturn(user);

        //when
        sut.addPoll(pollDTO, principal);

        //then
        verify(pollRepository).save(pollCaptor.capture());
        var savedPoll = pollCaptor.getValue();

        assertThat(savedPoll.getText()).isEqualTo(EXAMPLE_TEXT);
        assertThat(savedPoll.getOwner()).isEqualTo(user);
        assertThat(savedPoll.getPollAnswers()).map(PollAnswer::getText).containsExactlyElementsOf(EXAMPLE_ANSWERS);
        assertThat(savedPoll.getPollAnswers()).map(PollAnswer::getVotesTotal).allMatch(total -> total == 0L);
    }

    @Test
    public void shouldThrowExceptionWhenUserDoesNotExist() throws Exception {
        //given
        var pollDTO  = PollDto.builder()
                .text(EXAMPLE_TEXT)
                .pollAnswers(createPollAnswerDtoList(EXAMPLE_ANSWERS))
                .build();

        when(principal.getName()).thenReturn(EXAMPLE_USERNAME);

        when(userService.findUserByUsername(EXAMPLE_USERNAME))
                .thenThrow(new UserDoesNotExistException(EXAMPLE_USERNAME));

        //then
        assertThrows(UserDoesNotExistException.class, () -> sut.addPoll(pollDTO, principal));
    }

    @Test
    public void pollAnswerVotesTotalShouldBeIncrementedWhenVoted() throws Exception {
        //given
        var id = 1L;
        when(pollAnswerRepository.existsById(id)).thenReturn(true);

        //when
        sut.vote(id);

        //then
        verify(pollAnswerRepository).incrementVotes(id);
    }

    @Test
    public void shouldThrowExceptionWhenPollAnswerDoesNotExist() {
        //given
        var id = 1L;
        when(pollAnswerRepository.existsById(id)).thenReturn(false);

        //then
        assertThrows(PollAnswerDoesNotExistException.class, () -> sut.vote(id));
    }

    @Test
    public void shouldCorrectlyCalculatePollResults() throws Exception {
        //given
        var id = 1L;
        var owner = createUser(EXAMPLE_USERNAME);
        var poll = Poll.builder()
                .text(EXAMPLE_TEXT)
                .pollAnswers(List.of(
                        createPollAnswer("Answer 1", 1L),
                        createPollAnswer("Answer 2", 2L),
                        createPollAnswer("Answer 3", 3L),
                        createPollAnswer("Answer 4", 2L)
                ))
                .build();
        when(pollRepository.findById(id)).thenReturn(Optional.of(poll));

        //when
        var results = sut.getPollResults(id);

        //then
        var answersResults = results.getResults();
        assertThat(answersResults).hasSize(4);

        assertThat(answersResults.get(0).getPollAnswerText()).isEqualTo("Answer 1");
        assertThat(answersResults.get(0).getPercentage()).isEqualTo(BigDecimal.valueOf(12.5));

        assertThat(answersResults.get(1).getPollAnswerText()).isEqualTo("Answer 2");
        assertThat(answersResults.get(1).getPercentage()).isEqualTo(BigDecimal.valueOf(25));

        assertThat(answersResults.get(2).getPollAnswerText()).isEqualTo("Answer 3");
        assertThat(answersResults.get(2).getPercentage()).isEqualTo(BigDecimal.valueOf(37.5));

        assertThat(answersResults.get(3).getPollAnswerText()).isEqualTo("Answer 4");
        assertThat(answersResults.get(3).getPercentage()).isEqualTo(BigDecimal.valueOf(25));

        assertThat(results.getVotesTotal()).isEqualTo(8L);
    }

    @Test
    public void shouldCorrectlyCalculateResultsWhenNoVotes() throws Exception {
        //given
        var id = 1L;
        var owner = createUser(EXAMPLE_USERNAME);
        var poll = Poll.builder()
                .text(EXAMPLE_TEXT)
                .pollAnswers(List.of(
                        createPollAnswer("Answer 1", 0L),
                        createPollAnswer("Answer 2", 0L),
                        createPollAnswer("Answer 3", 0L),
                        createPollAnswer("Answer 4", 0L)
                ))
                .build();
        when(pollRepository.findById(id)).thenReturn(Optional.of(poll));

        //when
        var results = sut.getPollResults(id);

        //then
        var answersResults = results.getResults();
        assertThat(answersResults).hasSize(4);

        assertThat(answersResults.get(0).getPollAnswerText()).isEqualTo("Answer 1");
        assertThat(answersResults.get(0).getPercentage()).isEqualTo(BigDecimal.ZERO);

        assertThat(answersResults.get(1).getPollAnswerText()).isEqualTo("Answer 2");
        assertThat(answersResults.get(1).getPercentage()).isEqualTo(BigDecimal.ZERO);

        assertThat(answersResults.get(2).getPollAnswerText()).isEqualTo("Answer 3");
        assertThat(answersResults.get(2).getPercentage()).isEqualTo(BigDecimal.ZERO);

        assertThat(answersResults.get(3).getPollAnswerText()).isEqualTo("Answer 4");
        assertThat(answersResults.get(3).getPercentage()).isEqualTo(BigDecimal.ZERO);

        assertThat(results.getVotesTotal()).isEqualTo(0L);
    }

    @Test
    public void shouldThrowExceptionWhenPollDoesNotExist() {
        //given
        var id = 1L;
        when(pollRepository.findById(id)).thenReturn(Optional.empty());

        //then
        assertThrows(PollDoesNotExistException.class, () -> sut.getPollResults(id));
    }

    @Test
    public void shouldDeletePoll() throws Exception {
        //given
        var id = 1L;
        var user = createUser(EXAMPLE_USERNAME);
        var poll = Poll.builder().owner(user).build();

        when(principal.getName()).thenReturn(EXAMPLE_USERNAME);
        when(userService.findUserByUsername(EXAMPLE_USERNAME)).thenReturn(user);
        when(pollRepository.findById(id)).thenReturn(Optional.of(poll));

        //when
        sut.deletePoll(id, principal);

        //then
        verify(pollRepository).deleteById(id);
    }

    @Test
    public void shouldThrowExceptionWhenUserTriesToDeleteNotHisPoll() throws Exception {
        //given
        var id = 1L;
        var user = createUser(EXAMPLE_USERNAME);
        var owner = createUser("owner");
        var poll = Poll.builder().owner(owner).build();

        when(principal.getName()).thenReturn(EXAMPLE_USERNAME);
        when(userService.findUserByUsername(EXAMPLE_USERNAME)).thenReturn(user);
        when(pollRepository.findById(id)).thenReturn(Optional.of(poll));

        //then
        assertThrows(UserNotAuthorizedException.class, () -> sut.deletePoll(id, principal));
    }

    private List<PollAnswerDto> createPollAnswerDtoList(List<String> answers) {
        return answers.stream()
                .map(this::createPollAnswerDto)
                .collect(Collectors.toList());
    }

    private List<PollAnswer> createPollAnswerList(List<String> answers) {
        return answers.stream()
                .map(answer -> createPollAnswer(answer, 0L))
                .collect(Collectors.toList());
    }

    private PollAnswerDto createPollAnswerDto(String answer) {
        return PollAnswerDto.builder()
                .text(answer)
                .build();
    }
    private PollAnswer createPollAnswer(String text, Long votesTotal) {
        return PollAnswer.builder()
                .text(text)
                .votesTotal(votesTotal)
                .build();
    }

    private User createUser(String userName) {
        return User.builder().username(userName).build();
    }

}