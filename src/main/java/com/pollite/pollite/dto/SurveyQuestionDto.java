package com.pollite.pollite.dto;

import com.pollite.pollite.model.survey.question.SurveyQuestionType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SurveyQuestionDto {

    private SurveyQuestionType type;

    private Long id;

    private String text;

    private Integer order;

    private List<SurveyQuestionAnswerDto> answers;
}
