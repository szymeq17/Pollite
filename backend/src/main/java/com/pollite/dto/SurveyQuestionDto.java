package com.pollite.dto;

import com.pollite.model.survey.question.SurveyQuestionType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SurveyQuestionDto {

    private Long id;

    private SurveyQuestionType type;

    private String text;

    private Integer order;

    private List<SurveyQuestionAnswerDto> answers;
}
