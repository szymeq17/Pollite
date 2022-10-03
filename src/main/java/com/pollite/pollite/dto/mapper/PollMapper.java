package com.pollite.pollite.dto.mapper;

import com.pollite.pollite.dto.PollDto;
import com.pollite.pollite.model.Poll;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(uses = PollAnswerMapper.class,
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        componentModel = "spring")
public interface PollMapper {

    @Mapping(source = "owner.username", target = "ownerUsername")
    PollDto toDto(Poll poll);

    Poll fromDto(PollDto pollDTO);
}
