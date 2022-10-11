package com.pollite.pollite.dto.mapper;

import com.pollite.pollite.dto.SurveyQuestionDto;
import com.pollite.pollite.model.survey.question.SurveyMultiChoiceQuestion;
import com.pollite.pollite.model.survey.question.SurveyQuestion;
import com.pollite.pollite.model.survey.question.SurveySingleChoiceQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        componentModel = "spring")
public interface SurveyQuestionMapper {

    @Mapping(target = "type", constant = "SINGLE_CHOICE")
    SurveyQuestionDto toDto(SurveySingleChoiceQuestion surveyQuestion);

    @Mapping(target = "type", constant = "MULTI_CHOICE")
    SurveyQuestionDto toDto(SurveyMultiChoiceQuestion surveyQuestion);

    @Named("surveyQuestion")
    default SurveyQuestion fromDto(SurveyQuestionDto surveyQuestionDto) {
        return switch (surveyQuestionDto.getType()) {
            case SINGLE_CHOICE -> toSurveySingleChoiceQuestion(surveyQuestionDto);
            case MULTI_CHOICE -> toSurveyMultiChoiceQuestion(surveyQuestionDto);
        };
    }

    SurveySingleChoiceQuestion toSurveySingleChoiceQuestion(SurveyQuestionDto surveyQuestionDto);

    SurveyMultiChoiceQuestion toSurveyMultiChoiceQuestion(SurveyQuestionDto surveyQuestionDto);
}
