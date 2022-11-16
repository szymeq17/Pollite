package com.pollite.pollite.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SurveyQuestionResultsDto {

    private Long questionId;
    private String questionText;
    private List<SurveyQuestionAnswerResultsDto> answersResults;
}
