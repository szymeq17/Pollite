package com.pollite.pollite.dto.mapper;

import com.pollite.pollite.dto.SurveyQuestionAnswerDto;
import com.pollite.pollite.model.survey.answer.SurveyQuestionAnswer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SurveyQuestionAnswerMapperTest {

    private final SurveyQuestionAnswerMapperImpl sut = new SurveyQuestionAnswerMapperImpl();

    @Test
    public void shouldCorrectlyMapDtoToSurveyQuestionAnswer() {
        //given
        var questionDto = SurveyQuestionAnswerDto.builder()
                .id(1L)
                .text("Answer")
                .build();

        //when
        var result = sut.fromDto(questionDto);

        //then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getText()).isEqualTo("Answer");
    }

    @Test
    public void shouldCorrectlyMapSurveyQuestionAnswerToDto() {
        //given
        var question = SurveyQuestionAnswer.builder()
                .id(1L)
                .text("Answer")
                .build();

        //when
        var result = sut.toDto(question);

        //then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getText()).isEqualTo("Answer");
    }
}