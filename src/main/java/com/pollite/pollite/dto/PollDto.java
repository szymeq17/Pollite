package com.pollite.pollite.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PollDto {
    private Long id;
    private String ownerUsername;
    private String text;
    private List<PollAnswerDto> pollAnswers;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
