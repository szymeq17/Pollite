package com.pollite.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PollAnswerResult {
    private String pollAnswerText;
    private Long votes;
    private BigDecimal percentage;
}
