package com.pollite.pollite.validator;

import com.pollite.pollite.dto.CompletedSurveyDto;
import com.pollite.pollite.dto.CompletedSurveyQuestionDto;
import com.pollite.pollite.exception.InvalidCompletedSurveyException;
import com.pollite.pollite.exception.SurveyNotActiveException;
import com.pollite.pollite.model.survey.Survey;
import com.pollite.pollite.model.survey.SurveyConfiguration;
import com.pollite.pollite.model.survey.SurveyQuestionExclusion;
import com.pollite.pollite.model.survey.answer.SurveyQuestionAnswer;
import com.pollite.pollite.model.survey.question.SurveyQuestion;
import com.pollite.pollite.model.survey.question.SurveyQuestionType;
import com.pollite.pollite.repository.SurveyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompletedSurveyValidatorTest {

    @InjectMocks
    private CompletedSurveyValidator sut;

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private Clock clock;

    private final Survey survey = Survey.builder()
            .id(1L)
            .questions(List.of(
                    SurveyQuestion.builder()
                            .id(1L)
                            .order(1)
                            .type(SurveyQuestionType.SINGLE_CHOICE)
                            .answers(List.of(
                                    SurveyQuestionAnswer.builder().id(1L).order(1).build(),
                                    SurveyQuestionAnswer.builder().id(2L).order(2).build(),
                                    SurveyQuestionAnswer.builder().id(3L).order(3).build()
                            ))
                            .build(),
                    SurveyQuestion.builder()
                            .id(2L)
                            .order(2)
                            .type(SurveyQuestionType.MULTI_CHOICE)
                            .answers(List.of(
                                    SurveyQuestionAnswer.builder().id(4L).order(2).build(),
                                    SurveyQuestionAnswer.builder().id(5L).order(3).build(),
                                    SurveyQuestionAnswer.builder().id(6L).order(1).build()
                            ))
                            .build(),
                    SurveyQuestion.builder()
                            .id(3L)
                            .order(3)
                            .type(SurveyQuestionType.SINGLE_CHOICE)
                            .answers(List.of(
                                    SurveyQuestionAnswer.builder().id(7L).order(3).build(),
                                    SurveyQuestionAnswer.builder().id(8L).order(2).build(),
                                    SurveyQuestionAnswer.builder().id(9L).order(1).build()
                            ))
                            .build()
            ))
            .configuration(SurveyConfiguration.builder()
                    .isActive(true)
                    .startDate(OffsetDateTime.MIN)
                    .endDate(OffsetDateTime.MIN.plusDays(7))
                    .exclusions(List.of(
                            SurveyQuestionExclusion.builder()
                                    .questionOrder(1)
                                    .answerOrder(1)
                                    .excludedQuestionOrder(3)
                                    .build(),
                            SurveyQuestionExclusion.builder()
                                    .questionOrder(2)
                                    .answerOrder(2)
                                    .excludedQuestionOrder(1)
                                    .build()
                    ))
                    .build())
            .build();

    @Test
    public void shouldNotThrowExceptionWhenCompletedSurveyIsCorrect() throws Exception {
        //given
        var completedSurveyDto = CompletedSurveyDto.builder()
                .surveyId(1L)
                .completedQuestions(List.of(
                        CompletedSurveyQuestionDto.builder()
                                .questionId(1L)
                                .questionAnswerIds(Set.of(2L))
                                .build(),
                        CompletedSurveyQuestionDto.builder()
                                .questionId(2L)
                                .questionAnswerIds(Set.of(5L, 6L))
                                .build(),
                        CompletedSurveyQuestionDto.builder()
                                .questionId(3L)
                                .questionAnswerIds(Set.of(7L))
                                .build()
                ))
                .build();

        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));
        when(clock.instant()).thenReturn(OffsetDateTime.MIN.plusDays(1).toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        //then
        sut.validate(completedSurveyDto);
    }

    @Test
    public void shouldNotThrowExceptionWhenCompletedSurveyIsCorrectWithExclusion() throws Exception {
        //given
        var completedSurveyDto = CompletedSurveyDto.builder()
                .surveyId(1L)
                .completedQuestions(List.of(
                        CompletedSurveyQuestionDto.builder()
                                .questionId(1L)
                                .questionAnswerIds(Set.of(1L))
                                .build(),
                        CompletedSurveyQuestionDto.builder()
                                .questionId(2L)
                                .questionAnswerIds(Set.of(5L, 6L))
                                .build()
                ))
                .build();

        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));
        when(clock.instant()).thenReturn(OffsetDateTime.MIN.plusDays(1).toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        //then
        sut.validate(completedSurveyDto);
    }

    @Test
    public void shouldThrowExceptionWhenSurveyIsNotActive() {
        //given
        var completedSurveyDto = CompletedSurveyDto.builder().surveyId(1L).build();

        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));
        when(clock.instant()).thenReturn(OffsetDateTime.MIN.plusDays(8).toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        //then
        assertThrows(SurveyNotActiveException.class, () -> sut.validate(completedSurveyDto));
    }

    @Test
    public void shouldThrowExceptionWhenThereAreTooManyAnswers() {
        //given
        var completedSurveyDto = CompletedSurveyDto.builder()
                .surveyId(1L)
                .completedQuestions(List.of(
                        CompletedSurveyQuestionDto.builder()
                                .questionId(1L)
                                .questionAnswerIds(Set.of(1L))
                                .build(),
                        CompletedSurveyQuestionDto.builder()
                                .questionId(2L)
                                .questionAnswerIds(Set.of(5L, 6L))
                                .build(),
                        CompletedSurveyQuestionDto.builder()
                                .questionId(3L)
                                .questionAnswerIds(Set.of(7L))
                                .build()
                ))
                .build();

        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));
        when(clock.instant()).thenReturn(OffsetDateTime.MIN.plusDays(1).toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        //then
        assertThrows(InvalidCompletedSurveyException.class, () -> sut.validate(completedSurveyDto));
    }

    @Test
    public void shouldThrowExceptionWhenThereAreNotEnoughAnswers() {
        //given
        var completedSurveyDto = CompletedSurveyDto.builder()
                .surveyId(1L)
                .completedQuestions(List.of(
                        CompletedSurveyQuestionDto.builder()
                                .questionId(1L)
                                .questionAnswerIds(Set.of(2L))
                                .build(),
                        CompletedSurveyQuestionDto.builder()
                                .questionId(2L)
                                .questionAnswerIds(Set.of(5L, 6L))
                                .build()
                ))
                .build();

        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));
        when(clock.instant()).thenReturn(OffsetDateTime.MIN.plusDays(1).toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        //then
        assertThrows(InvalidCompletedSurveyException.class, () -> sut.validate(completedSurveyDto));
    }

    @Test
    public void shouldThrowExceptionWhenNotAllQuestionsHaveAnswers() {
        //given
        var completedSurveyDto = CompletedSurveyDto.builder()
                .surveyId(1L)
                .completedQuestions(List.of(
                        CompletedSurveyQuestionDto.builder()
                                .questionId(1L)
                                .questionAnswerIds(Set.of(2L))
                                .build(),
                        CompletedSurveyQuestionDto.builder()
                                .questionId(2L)
                                .questionAnswerIds(Set.of(5L, 6L))
                                .build(),
                        CompletedSurveyQuestionDto.builder()
                                .questionId(4L)
                                .questionAnswerIds(Set.of(5L, 6L))
                                .build()
                ))
                .build();

        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));
        when(clock.instant()).thenReturn(OffsetDateTime.MIN.plusDays(1).toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        //then
        assertThrows(InvalidCompletedSurveyException.class, () -> sut.validate(completedSurveyDto));
    }

}