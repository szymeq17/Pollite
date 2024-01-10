package com.pollite.pollite.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SurveyQuestionExclusionDto {

    private Long id;

    private Long questionOrder;

    private Long answerOrder;

    private Long excludedQuestionOrder;
}
