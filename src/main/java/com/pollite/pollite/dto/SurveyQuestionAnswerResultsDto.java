package com.pollite.pollite.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SurveyQuestionAnswerResultsDto {

    private Long answerId;
    private String answerText;
    private Integer total;
}
