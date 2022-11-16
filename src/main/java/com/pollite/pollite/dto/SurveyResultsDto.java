package com.pollite.pollite.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SurveyResultsDto {

    private List<SurveyQuestionResultsDto> questionsResults;
}
