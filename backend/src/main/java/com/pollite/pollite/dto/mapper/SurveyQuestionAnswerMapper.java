package com.pollite.pollite.dto.mapper;

import com.pollite.pollite.dto.SurveyQuestionAnswerDto;
import com.pollite.pollite.model.survey.answer.SurveyQuestionAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        componentModel = "spring")
public interface SurveyQuestionAnswerMapper {

    SurveyQuestionAnswerDto toDto(SurveyQuestionAnswer surveyQuestionAnswer);

    SurveyQuestionAnswer fromDto(SurveyQuestionAnswerDto surveyQuestionAnswerDto);
}
