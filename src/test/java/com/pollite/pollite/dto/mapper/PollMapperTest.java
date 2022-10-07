package com.pollite.pollite.dto.mapper;

import com.pollite.pollite.dto.PollAnswerDto;
import com.pollite.pollite.dto.PollDto;
import com.pollite.pollite.model.Poll;
import com.pollite.pollite.model.PollAnswer;
import com.pollite.pollite.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PollMapperTest {

    private static final String POLL_TEXT = "Example poll text";
    private static final OffsetDateTime POLL_START_DATE = OffsetDateTime.of(LocalDateTime.of(2022, 1, 6, 12, 0, 0), ZoneOffset.ofHours(2));
    private static final OffsetDateTime POLL_END_DATE = POLL_START_DATE.plusDays(10);
    private static final Long POLL_ID = 1L;
    private static final String USERNAME = "username";

    @InjectMocks
    private PollMapperImpl sut;

    @Mock
    private PollAnswerMapper pollAnswerMapper;

    @Test
    public void shouldCorrectlyMapDtoToPoll() {
        //given
        var pollAnswerDto1 = createPollAnswerDto(1L, "Answer 1");
        var pollAnswerDto2 = createPollAnswerDto(2L, "Answer 2");
        var pollAnswerDto3 = createPollAnswerDto(3L, "Answer 3");

        var pollAnswer1 = createPollAnswer(1L, "Answer 1");
        var pollAnswer2 = createPollAnswer(2L, "Answer 2");
        var pollAnswer3 = createPollAnswer(3L, "Answer 3");

        var pollDto = PollDto.builder()
                .id(1L)
                .text(POLL_TEXT)
                .startDateTime(POLL_START_DATE)
                .endDateTime(POLL_END_DATE)
                .pollAnswers(List.of(pollAnswerDto1, pollAnswerDto2, pollAnswerDto3))
                .build();

        when(pollAnswerMapper.fromDto(pollAnswerDto1)).thenReturn(pollAnswer1);
        when(pollAnswerMapper.fromDto(pollAnswerDto2)).thenReturn(pollAnswer2);
        when(pollAnswerMapper.fromDto(pollAnswerDto3)).thenReturn(pollAnswer3);

        //when
        var result = sut.fromDto(pollDto);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(POLL_ID);
        assertThat(result.getText()).isEqualTo(POLL_TEXT);
        assertThat(result.getStartDateTime()).isEqualTo(POLL_START_DATE);
        assertThat(result.getEndDateTime()).isEqualTo(POLL_END_DATE);
        assertThat(result.getPollAnswers()).containsAll(List.of(pollAnswer1, pollAnswer2, pollAnswer3));
    }

    @Test
    public void shouldCorrectlyMapPollToDto() {
        //given
        var pollAnswer1 = createPollAnswer(1L, "Answer 1");
        var pollAnswer2 = createPollAnswer(2L, "Answer 2");
        var pollAnswer3 = createPollAnswer(3L, "Answer 3");

        var pollAnswerDto1 = createPollAnswerDto(1L, "Answer 1");
        var pollAnswerDto2 = createPollAnswerDto(2L, "Answer 2");
        var pollAnswerDto3 = createPollAnswerDto(3L, "Answer 3");

        var owner = User.builder().username(USERNAME).build();

        var poll = Poll.builder()
                .text(POLL_TEXT)
                .startDateTime(POLL_START_DATE)
                .endDateTime(POLL_END_DATE)
                .owner(owner)
                .id(POLL_ID)
                .pollAnswers(List.of(pollAnswer1, pollAnswer2, pollAnswer3))
                .build();

        when(pollAnswerMapper.toDto(pollAnswer1)).thenReturn(pollAnswerDto1);
        when(pollAnswerMapper.toDto(pollAnswer2)).thenReturn(pollAnswerDto2);
        when(pollAnswerMapper.toDto(pollAnswer3)).thenReturn(pollAnswerDto3);

        //when
        var result = sut.toDto(poll);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(POLL_ID);
        assertThat(result.getText()).isEqualTo(POLL_TEXT);
        assertThat(result.getStartDateTime()).isEqualTo(POLL_START_DATE);
        assertThat(result.getEndDateTime()).isEqualTo(POLL_END_DATE);
        assertThat(result.getOwnerUsername()).isEqualTo(USERNAME);
        assertThat(result.getPollAnswers()).containsAll(List.of(pollAnswerDto1, pollAnswerDto2, pollAnswerDto3));
    }

    private PollDto createPollDto() {
        return PollDto.builder()
                .id(1L)
                .text(POLL_TEXT)
                .startDateTime(POLL_START_DATE)
                .endDateTime(POLL_END_DATE)
                .pollAnswers(List.of(
                        createPollAnswerDto(1L, "Answer 1"),
                        createPollAnswerDto(2L, "Answer 2"),
                        createPollAnswerDto(3L, "Answer 3")
                ))
                .build();
    }

    private PollAnswerDto createPollAnswerDto(Long id, String text) {
        return PollAnswerDto.builder().text(text).id(id).build();
    }

    private PollAnswer createPollAnswer(Long id, String text) {
        return PollAnswer.builder().id(id).text(text).votesTotal(0L).build();
    }
}