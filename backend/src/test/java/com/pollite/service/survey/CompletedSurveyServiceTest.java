package com.pollite.service.survey;

import com.pollite.dto.CompletedSurveyDto;
import com.pollite.dto.mapper.CompletedSurveyMapper;
import com.pollite.dto.CompletedSurveyDto;
import com.pollite.dto.mapper.CompletedSurveyMapper;
import com.pollite.model.survey.CompletedSurvey;
import com.pollite.model.survey.Survey;
import com.pollite.model.survey.answer.SurveyQuestionAnswer;
import com.pollite.model.survey.question.CompletedQuestion;
import com.pollite.model.survey.question.SurveyQuestion;
import com.pollite.repository.CompletedSurveyRepository;
import com.pollite.repository.SurveyRepository;
import com.pollite.validator.CompletedSurveyValidator;
import com.pollite.repository.CompletedSurveyRepository;
import com.pollite.repository.SurveyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompletedSurveyServiceTest {

    private static final Long SURVEY_ID = 1L;

    @Mock
    private CompletedSurveyRepository completedSurveyRepository;

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private CompletedSurveyMapper completedSurveyMapper;

    @Mock
    private CompletedSurveyValidator completedSurveyValidator;

    @InjectMocks
    private CompletedSurveyService sut;

    @Test
    void shouldSaveSubmittedSurvey() {
        //given
        var completedSurveyDto = CompletedSurveyDto.builder().build();
        var completedSurvey = CompletedSurvey.builder().build();
        doNothing().when(completedSurveyValidator).validate(completedSurveyDto);
        when(completedSurveyMapper.fromDto(completedSurveyDto)).thenReturn(completedSurvey);

        //when
        sut.submitCompletedSurvey(completedSurveyDto);

        //then
        verify(completedSurveyRepository).save(completedSurvey);
    }

    @Test
    void shouldProperlyCalculateSurveyResultsWithoutFilters() {
        //given
        var survey = createSurvey();
        when(surveyRepository.findById(SURVEY_ID)).thenReturn(Optional.of(survey));
        when(completedSurveyRepository.findAllBySurveyId(SURVEY_ID))
                .thenReturn(createCompletedSurveys(survey));

        //when
        var result = sut.getSurveyResults(SURVEY_ID, Collections.emptyList());

        //then
        var question1 = result.findQuestionResultsById(1L);
        var question2 = result.findQuestionResultsById(2L);
        var question3 = result.findQuestionResultsById(3L);

        var question1Answer1 = question1.findAnswerResultById(1L);
        var question1Answer2 = question1.findAnswerResultById(2L);
        var question1Answer3 = question1.findAnswerResultById(3L);

        var question2Answer1 = question2.findAnswerResultById(4L);
        var question2Answer2 = question2.findAnswerResultById(5L);
        var question2Answer3 = question2.findAnswerResultById(6L);

        var question3Answer1 = question3.findAnswerResultById(7L);
        var question3Answer2 = question3.findAnswerResultById(8L);
        var question3Answer3 = question3.findAnswerResultById(9L);

        Assertions.assertThat(question1.getTotal()).isEqualTo(2L);
        Assertions.assertThat(question2.getTotal()).isEqualTo(2L);
        Assertions.assertThat(question3.getTotal()).isEqualTo(2L);

        Assertions.assertThat(question1Answer1.getTotal()).isEqualTo(2);
        Assertions.assertThat(question1Answer1.getPercentage().longValue()).isEqualTo(100L);
        Assertions.assertThat(question1Answer2.getTotal()).isEqualTo(0);
        Assertions.assertThat(question1Answer2.getPercentage().longValue()).isEqualTo(0L);
        Assertions.assertThat(question1Answer3.getTotal()).isEqualTo(0);
        Assertions.assertThat(question1Answer3.getPercentage().longValue()).isEqualTo(0L);
    }

    private Survey createSurvey() {
        return Survey.builder()
                .questions(
                        List.of(
                                SurveyQuestion.builder()
                                        .id(1L)
                                        .order(1)
                                        .text("Question 1")
                                        .answers(
                                                List.of(
                                                        SurveyQuestionAnswer.builder()
                                                                .id(1L)
                                                                .text("Answer 1")
                                                                .order(1)
                                                                .build(),
                                                        SurveyQuestionAnswer.builder()
                                                                .id(2L)
                                                                .text("Answer 2")
                                                                .order(2)
                                                                .build(),
                                                        SurveyQuestionAnswer.builder()
                                                                .id(3L)
                                                                .text("Answer 3")
                                                                .order(3)
                                                                .build()
                                                )
                                        )
                                        .build(),
                                SurveyQuestion.builder()
                                        .id(2L)
                                        .order(2)
                                        .text("Question 2")
                                        .answers(
                                                List.of(
                                                        SurveyQuestionAnswer.builder()
                                                                .id(4L)
                                                                .text("Answer 1")
                                                                .order(1)
                                                                .build(),
                                                        SurveyQuestionAnswer.builder()
                                                                .id(5L)
                                                                .text("Answer 2")
                                                                .order(2)
                                                                .build(),
                                                        SurveyQuestionAnswer.builder()
                                                                .id(6L)
                                                                .text("Answer 3")
                                                                .order(3)
                                                                .build()
                                                )
                                        )
                                        .build(),
                                SurveyQuestion.builder()
                                        .id(2L)
                                        .order(2)
                                        .text("Question 2")
                                        .answers(
                                                List.of(
                                                        SurveyQuestionAnswer.builder()
                                                                .id(4L)
                                                                .text("Answer 1")
                                                                .order(1)
                                                                .build(),
                                                        SurveyQuestionAnswer.builder()
                                                                .id(5L)
                                                                .text("Answer 2")
                                                                .order(2)
                                                                .build(),
                                                        SurveyQuestionAnswer.builder()
                                                                .id(6L)
                                                                .text("Answer 3")
                                                                .order(3)
                                                                .build()
                                                )
                                        )
                                        .build(),
                                SurveyQuestion.builder()
                                        .id(3L)
                                        .order(3)
                                        .text("Question 3")
                                        .answers(
                                                List.of(
                                                        SurveyQuestionAnswer.builder()
                                                                .id(7L)
                                                                .text("Answer 1")
                                                                .order(1)
                                                                .build(),
                                                        SurveyQuestionAnswer.builder()
                                                                .id(8L)
                                                                .text("Answer 2")
                                                                .order(2)
                                                                .build(),
                                                        SurveyQuestionAnswer.builder()
                                                                .id(9L)
                                                                .text("Answer 3")
                                                                .order(3)
                                                                .build()
                                                )
                                        )
                                        .build()
                        )
                )
                .build();
    }

    private List<CompletedSurvey> createCompletedSurveys(Survey survey) {
        return List.of(
                CompletedSurvey.builder()
                        .survey(survey)
                        .completedQuestions(
                                List.of(
                                        CompletedQuestion.builder()
                                                .question(survey.getQuestionById(1L))
                                                .answers(
                                                        List.of(
                                                                SurveyQuestionAnswer.builder()
                                                                        .id(1L)
                                                                        .text("Answer 1")
                                                                        .order(1)
                                                                        .build()
                                                        ))
                                                .build(),
                                        CompletedQuestion.builder()
                                                .question(survey.getQuestionById(2L))
                                                .answers(
                                                        List.of(
                                                                SurveyQuestionAnswer.builder()
                                                                        .id(5L)
                                                                        .text("Answer 2")
                                                                        .order(2)
                                                                        .build()
                                                        ))
                                                .build(),
                                        CompletedQuestion.builder()
                                                .question(survey.getQuestionById(3L))
                                                .answers(
                                                        List.of(
                                                                SurveyQuestionAnswer.builder()
                                                                        .id(9L)
                                                                        .text("Answer 3")
                                                                        .order(3)
                                                                        .build()
                                                        ))
                                                .build()
                                )
                        )
                        .build(),
                CompletedSurvey.builder()
                        .survey(survey)
                        .completedQuestions(
                                List.of(
                                        CompletedQuestion.builder()
                                                .question(survey.getQuestionById(1L))
                                                .answers(
                                                        List.of(
                                                                SurveyQuestionAnswer.builder()
                                                                        .id(1L)
                                                                        .text("Answer 1")
                                                                        .order(1)
                                                                        .build()
                                                        ))
                                                .build(),
                                        CompletedQuestion.builder()
                                                .question(survey.getQuestionById(2L))
                                                .answers(
                                                        List.of(
                                                                SurveyQuestionAnswer.builder()
                                                                        .id(4L)
                                                                        .text("Answer 1")
                                                                        .order(4)
                                                                        .build()
                                                        ))
                                                .build(),
                                        CompletedQuestion.builder()
                                                .question(survey.getQuestionById(3L))
                                                .answers(
                                                        List.of(
                                                                SurveyQuestionAnswer.builder()
                                                                        .id(9L)
                                                                        .text("Answer 3")
                                                                        .order(3)
                                                                        .build()
                                                        ))
                                                .build()
                                )
                        )
                        .build()
        );
    }
}