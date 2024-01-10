package com.pollite.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SurveyQuestionAnswerResultsDto {

    private Long answerId;
    private String answerText;
    private Integer total;
    private BigDecimal percentage;
}
