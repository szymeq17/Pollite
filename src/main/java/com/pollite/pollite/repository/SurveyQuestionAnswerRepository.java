package com.pollite.pollite.repository;

import com.pollite.pollite.model.survey.answer.SurveyQuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyQuestionAnswerRepository extends JpaRepository<SurveyQuestionAnswer, Long> {
}
