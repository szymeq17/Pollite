package com.pollite.pollite.repository;

import com.pollite.pollite.model.survey.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    Page<Survey> findAllByOwnerUsername(String username, Pageable pageable);
}
