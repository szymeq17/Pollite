package com.pollite.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SurveyDto {

    private Long id;

    private String ownerUsername;

    private String description;

    private List<SurveyQuestionDto> questions;

    private SurveyConfigurationDto configuration;
}
