package com.pollite.dto.mapper;

import com.pollite.dto.SurveyQuestionAnswerDto;
import com.pollite.dto.SurveyQuestionDto;
import com.pollite.model.survey.answer.SurveyQuestionAnswer;
import com.pollite.model.survey.question.SurveyQuestion;
import com.pollite.model.survey.question.SurveyQuestionType;
import com.pollite.dto.SurveyQuestionAnswerDto;
import com.pollite.dto.SurveyQuestionDto;
import com.pollite.model.survey.answer.SurveyQuestionAnswer;
import com.pollite.model.survey.question.SurveyQuestion;
import com.pollite.model.survey.question.SurveyQuestionType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SurveyQuestionMapperTest {

    private final SurveyQuestionMapperImpl sut = new SurveyQuestionMapperImpl();

    @Test
    public void shouldCorrectlyMapDtoToSurveySingleChoiceQuestion() {
        //given
        var questionDto = SurveyQuestionDto.builder()
                .type(SurveyQuestionType.SINGLE_CHOICE)
                .text("Question 1")
                .order(1)
                .answers(List.of(
                        SurveyQuestionAnswerDto.builder().text("Question 1 Answer 1").build(),
                        SurveyQuestionAnswerDto.builder().text("Question 1 Answer 2").build()
                ))
                .build();

        //when
        var result = sut.fromDto(questionDto);

        //then
        assertThat(result.getText()).isEqualTo("Question 1");
        assertThat(result.getType()).isEqualTo(SurveyQuestionType.SINGLE_CHOICE);
        Assertions.assertThat(result.getAnswers()).hasSize(2);
        assertThat(result.getOrder()).isEqualTo(1);
        Assertions.assertThat(result.getAnswers().get(0).getText()).isEqualTo("Question 1 Answer 1");
        Assertions.assertThat(result.getAnswers().get(1).getText()).isEqualTo("Question 1 Answer 2");
    }

    @Test
    public void shouldCorrectlyMapDtoToSurveyMultiChoiceQuestion() {
        //given
        var questionDto = SurveyQuestionDto.builder()
                .type(SurveyQuestionType.MULTI_CHOICE)
                .text("Question 1")
                .order(1)
                .answers(List.of(
                        SurveyQuestionAnswerDto.builder().text("Question 1 Answer 1").build(),
                        SurveyQuestionAnswerDto.builder().text("Question 1 Answer 2").build()
                ))
                .build();

        //when
        var result = sut.fromDto(questionDto);

        //then
        assertThat(result.getText()).isEqualTo("Question 1");
        assertThat(result.getType()).isEqualTo(SurveyQuestionType.MULTI_CHOICE);
        Assertions.assertThat(result.getAnswers()).hasSize(2);
        assertThat(result.getOrder()).isEqualTo(1);
        Assertions.assertThat(result.getAnswers().get(0).getText()).isEqualTo("Question 1 Answer 1");
        Assertions.assertThat(result.getAnswers().get(1).getText()).isEqualTo("Question 1 Answer 2");
    }

    @Test
    public void shouldCorrectlyMapSurveySingleChoiceQuestionToDto() {
        //given
        var question = SurveyQuestion.builder()
                .type(SurveyQuestionType.SINGLE_CHOICE)
                .text("Question 1")
                .order(1)
                .answers(List.of(
                        SurveyQuestionAnswer.builder().text("Question 1 Answer 1").build(),
                        SurveyQuestionAnswer.builder().text("Question 1 Answer 2").build()
                ))
                .build();

        //when
        var result = sut.toDto(question);

        //then
        assertThat(result.getText()).isEqualTo("Question 1");
        assertThat(result.getType()).isEqualTo(SurveyQuestionType.SINGLE_CHOICE);
        assertThat(result.getAnswers()).hasSize(2);
        assertThat(result.getOrder()).isEqualTo(1);
        assertThat(result.getAnswers().get(0).getText()).isEqualTo("Question 1 Answer 1");
        assertThat(result.getAnswers().get(1).getText()).isEqualTo("Question 1 Answer 2");
    }

    @Test
    public void shouldCorrectlyMapSurveyMultiChoiceQuestionToDto() {
        //given
        var question = SurveyQuestion.builder()
                .type(SurveyQuestionType.MULTI_CHOICE)
                .text("Question 1")
                .order(1)
                .answers(List.of(
                        SurveyQuestionAnswer.builder().text("Question 1 Answer 1").build(),
                        SurveyQuestionAnswer.builder().text("Question 1 Answer 2").build()
                ))
                .build();

        //when
        var result = sut.toDto(question);

        //then
        assertThat(result.getText()).isEqualTo("Question 1");
        assertThat(result.getType()).isEqualTo(SurveyQuestionType.MULTI_CHOICE);
        assertThat(result.getAnswers()).hasSize(2);
        assertThat(result.getOrder()).isEqualTo(1);
        assertThat(result.getAnswers().get(0).getText()).isEqualTo("Question 1 Answer 1");
        assertThat(result.getAnswers().get(1).getText()).isEqualTo("Question 1 Answer 2");
    }
}