package com.pollite.pollite.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PollAnswerDto {
    private Long id;
    private String text;
}
