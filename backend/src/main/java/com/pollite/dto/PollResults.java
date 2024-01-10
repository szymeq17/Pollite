package com.pollite.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PollResults {
    private List<PollAnswerResult> results;
    private Long votesTotal;
}
