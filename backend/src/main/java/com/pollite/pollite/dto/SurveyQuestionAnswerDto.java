package com.pollite.pollite.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SurveyQuestionAnswerDto {

    private Long id;

    private String text;

    private Integer order;
}
