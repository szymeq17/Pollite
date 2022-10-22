package com.pollite.pollite.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
public class CompletedSurveyQuestionDto {

    @NotNull
    private Long questionId;

    @NotEmpty
    private Set<Long> questionAnswerIds;
}
