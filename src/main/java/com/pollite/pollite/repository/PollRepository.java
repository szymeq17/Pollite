package com.pollite.pollite.repository;

import com.pollite.pollite.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Long> {
}
