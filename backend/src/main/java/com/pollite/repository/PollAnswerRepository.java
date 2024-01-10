package com.pollite.repository;

import com.pollite.model.PollAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PollAnswerRepository extends JpaRepository<PollAnswer, Long> {

    @Modifying
    @Query("UPDATE PollAnswer pa SET pa.votesTotal = pa.votesTotal + 1 WHERE pa.id = :id")
    void incrementVotes(Long id);
}
