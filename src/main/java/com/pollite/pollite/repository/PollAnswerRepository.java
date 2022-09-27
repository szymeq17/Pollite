package com.pollite.pollite.repository;

import com.pollite.pollite.model.PollAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollAnswerRepository extends JpaRepository<PollAnswer, Long> {
}
