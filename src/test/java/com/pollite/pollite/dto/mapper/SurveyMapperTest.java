package com.pollite.pollite.dto.mapper;

import com.pollite.pollite.dto.SurveyConfigurationDto;
import com.pollite.pollite.dto.SurveyDto;
import com.pollite.pollite.dto.SurveyQuestionAnswerDto;
import com.pollite.pollite.dto.SurveyQuestionDto;
import com.pollite.pollite.model.User;
import com.pollite.pollite.model.survey.Survey;
import com.pollite.pollite.model.survey.SurveyConfiguration;
import com.pollite.pollite.model.survey.answer.SurveyQuestionAnswer;
import com.pollite.pollite.model.survey.question.SurveyQuestion;
import com.pollite.pollite.model.survey.question.SurveyQuestionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class SurveyMapperTest {

    @InjectMocks
    private SurveyMapperImpl sut;

    @Mock
    private SurveyQuestionMapper surveyQuestionMapper;

    @Test
    public void shouldCorrectlyMapDtoToSurvey() {
        //given
        var questionDto1 = SurveyQuestionDto.builder()
                .type(SurveyQuestionType.SINGLE_CHOICE)
                .text("Question 1")
                .order(1)
                .answers(List.of(
                        SurveyQuestionAnswerDto.builder().text("Question 1 Answer 1").build(),
                        SurveyQuestionAnswerDto.builder().text("Question 1 Answer 2").build()
                ))
                .build();

        var questionDto2 = SurveyQuestionDto.builder()
                .type(SurveyQuestionType.MULTI_CHOICE)
                .text("Question 2")
                .order(2)
                .answers(List.of(
                        SurveyQuestionAnswerDto.builder().text("Question 2 Answer 1").build(),
                        SurveyQuestionAnswerDto.builder().text("Question 2 Answer 2").build()
                ))
                .build();

        var configDto = SurveyConfigurationDto.builder()
                .isActive(true)
                .startDate(OffsetDateTime.MIN)
                .endDate(OffsetDateTime.MAX)
                .build();

        var surveyDto = SurveyDto.builder()
                .ownerUsername("username")
                .questions(List.of(questionDto1, questionDto2))
                .configuration(configDto)
                .build();

        when(surveyQuestionMapper.fromDto(questionDto1)).thenReturn(
                SurveyQuestion.builder()
                        .type(SurveyQuestionType.SINGLE_CHOICE)
                        .text("Question 1")
                        .order(1)
                        .answers(List.of(
                                SurveyQuestionAnswer.builder().text("Question 1 Answer 1").build(),
                                SurveyQuestionAnswer.builder().text("Question 1 Answer 2").build()
                        ))
                        .build());

        when(surveyQuestionMapper.fromDto(questionDto2)).thenReturn(
                SurveyQuestion.builder()
                        .type(SurveyQuestionType.MULTI_CHOICE)
                        .text("Question 2")
                        .order(2)
                        .answers(List.of(
                                SurveyQuestionAnswer.builder().text("Question 2 Answer 1").build(),
                                SurveyQuestionAnswer.builder().text("Question 2 Answer 2").build()
                        ))
                        .build());

        //when
        var result = sut.fromDto(surveyDto);

        //then
        var questions = result.getQuestions();
        var config = result.getConfiguration();

        assertThat(questions).hasSize(2);

        var question1 = questions.get(0);
        var question2 = questions.get(1);


        assertThat(question1.getText()).isEqualTo("Question 1");
        assertThat(question1.getType()).isEqualTo(SurveyQuestionType.SINGLE_CHOICE);
        assertThat(question1.getAnswers()).hasSize(2);
        assertThat(question1.getOrder()).isEqualTo(1);
        assertThat(question1.getAnswers().get(0).getText()).isEqualTo("Question 1 Answer 1");
        assertThat(question1.getAnswers().get(1).getText()).isEqualTo("Question 1 Answer 2");

        assertThat(question2.getText()).isEqualTo("Question 2");
        assertThat(question2.getType()).isEqualTo(SurveyQuestionType.MULTI_CHOICE);
        assertThat(question2.getAnswers()).hasSize(2);
        assertThat(question2.getOrder()).isEqualTo(2);
        assertThat(question2.getAnswers().get(0).getText()).isEqualTo("Question 2 Answer 1");
        assertThat(question2.getAnswers().get(1).getText()).isEqualTo("Question 2 Answer 2");

        assertThat(config.getIsActive()).isEqualTo(true);
        assertThat(config.getStartDate()).isEqualTo(OffsetDateTime.MIN);
        assertThat(config.getEndDate()).isEqualTo(OffsetDateTime.MAX);
    }

    @Test
    public void shouldCorrectlyMapSurveyToDto() {
        //given
        var question1 = SurveyQuestion.builder()
                .type(SurveyQuestionType.SINGLE_CHOICE)
                .text("Question 1")
                .order(1)
                .answers(List.of(
                        SurveyQuestionAnswer.builder().text("Question 1 Answer 1").build(),
                        SurveyQuestionAnswer.builder().text("Question 1 Answer 2").build()
                ))
                .build();

        var question2 = SurveyQuestion.builder()
                .type(SurveyQuestionType.MULTI_CHOICE)
                .text("Question 2")
                .order(2)
                .answers(List.of(
                        SurveyQuestionAnswer.builder().text("Question 2 Answer 1").build(),
                        SurveyQuestionAnswer.builder().text("Question 2 Answer 2").build()
                ))
                .build();

        var config = SurveyConfiguration.builder()
                .isActive(true)
                .startDate(OffsetDateTime.MIN)
                .endDate(OffsetDateTime.MAX)
                .build();

        var survey = Survey.builder()
                .owner(User.builder().username("username").build())
                .questions(List.of(question1, question2))
                .configuration(config)
                .build();

        when(surveyQuestionMapper.toDto(question1)).thenReturn(
                SurveyQuestionDto.builder()
                        .type(SurveyQuestionType.SINGLE_CHOICE)
                        .text("Question 1")
                        .order(1)
                        .answers(List.of(
                                SurveyQuestionAnswerDto.builder().text("Question 1 Answer 1").build(),
                                SurveyQuestionAnswerDto.builder().text("Question 1 Answer 2").build()
                        ))
                        .build());

        when(surveyQuestionMapper.toDto(question2)).thenReturn(
                SurveyQuestionDto.builder()
                        .type(SurveyQuestionType.MULTI_CHOICE)
                        .text("Question 2")
                        .order(2)
                        .answers(List.of(
                                SurveyQuestionAnswerDto.builder().text("Question 2 Answer 1").build(),
                                SurveyQuestionAnswerDto.builder().text("Question 2 Answer 2").build()
                        ))
                        .build());

        //when
        var result = sut.toDto(survey);

        //then
        var questionDtos = result.getQuestions();
        var configDto = result.getConfiguration();

        assertThat(questionDtos).hasSize(2);

        var questionDto1 = questionDtos.get(0);
        var questionDto2 = questionDtos.get(1);

        assertThat(result.getOwnerUsername()).isEqualTo("username");

        assertThat(questionDto1.getText()).isEqualTo("Question 1");
        assertThat(questionDto1.getType()).isEqualTo(SurveyQuestionType.SINGLE_CHOICE);
        assertThat(questionDto1.getAnswers()).hasSize(2);
        assertThat(questionDto1.getOrder()).isEqualTo(1);
        assertThat(questionDto1.getAnswers().get(0).getText()).isEqualTo("Question 1 Answer 1");
        assertThat(question1.getAnswers().get(1).getText()).isEqualTo("Question 1 Answer 2");

        assertThat(questionDto2.getText()).isEqualTo("Question 2");
        assertThat(questionDto2.getType()).isEqualTo(SurveyQuestionType.MULTI_CHOICE);
        assertThat(questionDto2.getAnswers()).hasSize(2);
        assertThat(questionDto2.getOrder()).isEqualTo(2);
        assertThat(questionDto2.getAnswers().get(0).getText()).isEqualTo("Question 2 Answer 1");
        assertThat(questionDto2.getAnswers().get(1).getText()).isEqualTo("Question 2 Answer 2");

        assertThat(configDto.getIsActive()).isEqualTo(true);
        assertThat(configDto.getStartDate()).isEqualTo(OffsetDateTime.MIN);
        assertThat(configDto.getEndDate()).isEqualTo(OffsetDateTime.MAX);

    }
}