package com.pollite.dto;


import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class SurveyInfoDto {
    private Long surveyId;
    private String description;
    private boolean isActive;
    private String owner;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
}
