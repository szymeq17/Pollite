package com.pollite.dto.mapper;

import com.pollite.dto.PollAnswerDto;
import com.pollite.model.PollAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL, componentModel = "spring")
public interface PollAnswerMapper {

    PollAnswerDto toDto(PollAnswer pollAnswer);

    @Mapping(target = "votesTotal", constant = "0L")
    PollAnswer fromDto(PollAnswerDto pollAnswerDTO);
}
