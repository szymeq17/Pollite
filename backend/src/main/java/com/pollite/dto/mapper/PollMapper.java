package com.pollite.dto.mapper;

import com.pollite.dto.PollDto;
import com.pollite.dto.PollInfoDto;
import com.pollite.model.Poll;
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

    PollInfoDto toPollInfoDto(Poll poll);
}
