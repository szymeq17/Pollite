package com.pollite.pollite.repository;

import com.pollite.pollite.model.survey.question.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {
}
