package com.pollite.repository;

import com.pollite.model.survey.CompletedSurvey;
import com.pollite.projection.SurveyResultsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompletedSurveyRepository extends JpaRepository<CompletedSurvey, Long> {

    @Query(
            value = "SELECT sq.ID as questionId, sqa.ID as answerId, count(CQA.ANSWERS_ID) as total " +
                    "from survey s " +
                    "join survey_questions sqs on s.id = sqs.survey_id " +
                    "join survey_question sq on sqs.questions_id = sq.id " +
                    "join survey_question_answers sqas on sq.id = sqas.SURVEY_QUESTION_ID " +
                    "join survey_question_answer sqa on sqas.answers_id = sqa.id " +
                    "left join COMPLETED_QUESTION_ANSWERS CQA on sqa.id = CQA.ANSWERS_ID " +
                    "where s.id = :surveyId " +
                    "group by sq.ID, sqa.ID " +
                    "order by sq.QUESTION_ORDER, sqa.ANSWER_ORDER", nativeQuery = true
    )
    List<SurveyResultsProjection> countAnswers(Long surveyId);

    List<CompletedSurvey> findAllBySurveyId(Long surveyId);

    void deleteAllBySurveyId(Long surveyId);
}
