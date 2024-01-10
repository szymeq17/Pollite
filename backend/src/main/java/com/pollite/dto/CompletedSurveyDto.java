package com.pollite.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class CompletedSurveyDto {

    @NotNull
    private Long surveyId;

    @NotEmpty
    private List<CompletedSurveyQuestionDto> completedQuestions;

}
