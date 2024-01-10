package com.pollite.repository;

import com.pollite.model.Poll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Long> {

    Page<Poll> findAllByOwnerUsername(String username, Pageable pageable);
}
