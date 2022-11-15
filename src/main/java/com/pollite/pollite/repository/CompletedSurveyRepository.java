package com.pollite.pollite.repository;

import com.pollite.pollite.model.survey.CompletedSurvey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompletedSurveyRepository extends JpaRepository<CompletedSurvey, Long> {
}
