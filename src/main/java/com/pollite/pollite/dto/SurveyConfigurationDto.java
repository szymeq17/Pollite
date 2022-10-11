package com.pollite.pollite.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class SurveyConfigurationDto {

    private Long id;

    private Boolean isActive;

    private OffsetDateTime startDate;

    private OffsetDateTime endDate;
}
