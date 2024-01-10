package com.pollite.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class PollDto {
    private Long id;
    private String ownerUsername;
    private String text;
    private List<PollAnswerDto> pollAnswers;
    private OffsetDateTime startDateTime;
    private OffsetDateTime endDateTime;
}
