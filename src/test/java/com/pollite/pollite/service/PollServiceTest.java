package com.pollite.pollite.service;

import com.pollite.pollite.dto.PollTemplate;
import com.pollite.pollite.exception.PollAnswerDoesNotExistException;
import com.pollite.pollite.exception.UserDoesNotExistException;
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

import java.security.Principal;
import java.util.List;
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
    private Principal principal;

    @Captor
    private ArgumentCaptor<Poll> pollCaptor;

    @Test
    public void shouldAddPoll() throws Exception {
        //given
        var pollTemplate = PollTemplate.builder()
                .text(EXAMPLE_TEXT)
                .answers(EXAMPLE_ANSWERS)
                .build();

        var user = createUser(EXAMPLE_USERNAME);

        when(principal.getName()).thenReturn(EXAMPLE_USERNAME);

        when(userService.findUserByUsername(EXAMPLE_USERNAME)).thenReturn(user);

        //when
        sut.addPoll(pollTemplate, principal);

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
        var pollTemplate = PollTemplate.builder()
                .text(EXAMPLE_TEXT)
                .answers(EXAMPLE_ANSWERS)
                .build();

        when(principal.getName()).thenReturn(EXAMPLE_USERNAME);

        when(userService.findUserByUsername(EXAMPLE_USERNAME))
                .thenThrow(new UserDoesNotExistException(EXAMPLE_USERNAME));

        //then
        assertThrows(UserDoesNotExistException.class, () -> sut.addPoll(pollTemplate, principal));
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

    private Poll createPoll(String text, List<String> answers, User owner) {
        return Poll.builder()
                .text(text)
                .pollAnswers(
                        answers.stream()
                                .map(
                                        answer -> PollAnswer.builder()
                                                .text(answer)
                                                .votesTotal(0L).build()
                                )
                                .collect(Collectors.toList())
                )
                .owner(owner)
                .build();
    }

    private User createUser(String userName) {
        return User.builder().username(userName).build();
    }

}