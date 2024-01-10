package com.pollite.pollite.dto.mapper;

import com.pollite.pollite.dto.SurveyQuestionDto;
import com.pollite.pollite.model.survey.question.SurveyQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        componentModel = "spring")
public interface SurveyQuestionMapper {

    SurveyQuestionDto toDto(SurveyQuestion surveyQuestion);

    SurveyQuestion fromDto(SurveyQuestionDto surveyQuestionDto);
}
