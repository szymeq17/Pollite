package com.pollite.dto.mapper;

import com.pollite.dto.SurveyQuestionDto;
import com.pollite.model.survey.question.SurveyQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        componentModel = "spring")
public interface SurveyQuestionMapper {

    SurveyQuestionDto toDto(SurveyQuestion surveyQuestion);

    SurveyQuestion fromDto(SurveyQuestionDto surveyQuestionDto);
}
