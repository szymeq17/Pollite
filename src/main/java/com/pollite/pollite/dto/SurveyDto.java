package com.pollite.pollite.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SurveyDto {

    private Long id;

    private String ownerUsername;

    private List<SurveyQuestionDto> questions;

    private SurveyConfigurationDto configuration;
}
