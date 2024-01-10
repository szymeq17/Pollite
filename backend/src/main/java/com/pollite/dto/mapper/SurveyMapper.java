package com.pollite.dto.mapper;

import com.pollite.dto.SurveyDto;
import com.pollite.model.survey.Survey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(uses = SurveyQuestionMapper.class,
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        componentModel = "spring")
public interface SurveyMapper {

    @Mapping(source = "owner.username", target = "ownerUsername")
    SurveyDto toDto(Survey survey);

    Survey fromDto(SurveyDto surveyDto);
}
