package com.pollite.pollite.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class SurveyConfigurationDto {

    private Long id;

    private Boolean isActive;

    private OffsetDateTime startDate;

    private OffsetDateTime endDate;

    private List<SurveyQuestionExclusionDto> exclusions;
}
