package com.pollite.pollite.repository;

import com.pollite.pollite.model.survey.CompletedSurvey;
import com.pollite.pollite.model.survey.Survey;
import com.pollite.pollite.projection.SurveyResultsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompletedSurveyRepository extends JpaRepository<CompletedSurvey, Long> {

    @Query(
            value = "SELECT cq.QUESTION_ID as questionId, CQA.ANSWERS_ID as answerId, count(CQA) as total " +
                    "from completed_survey cs " +
                    "join completed_survey_completed_questions cscq on cs.id = cscq.COMPLETED_SURVEY_ID " +
                    "join COMPLETED_QUESTION cq on cscq.COMPLETED_QUESTIONS_ID = cq.ID " +
                    "join COMPLETED_QUESTION_ANSWERS CQA on cq.ID = CQA.COMPLETED_QUESTION_ID\n" +
                    "where cs.SURVEY_ID = :surveyId" +
                    "group by cq.QUESTION_ID, CQA.ANSWERS_ID", nativeQuery = true
    )
    public List<SurveyResultsProjection> countAnswers(Long surveyId);
}
