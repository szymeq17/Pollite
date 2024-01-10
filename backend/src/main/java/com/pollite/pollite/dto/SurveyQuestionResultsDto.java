package com.pollite.pollite.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@Builder
public class SurveyQuestionResultsDto {

    private Long questionId;
    private String questionText;
    private List<SurveyQuestionAnswerResultsDto> answersResults;
    private Integer total;

    public SurveyQuestionAnswerResultsDto findAnswerResultById(Long id) {
        return answersResults.stream()
                .filter(answersResult -> Objects.equals(answersResult.getAnswerId(), id))
                .findFirst()
                .orElse(null);
    }
}
