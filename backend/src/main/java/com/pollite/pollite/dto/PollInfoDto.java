package com.pollite.pollite.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PollInfoDto {
    Long id;
    String text;
}
