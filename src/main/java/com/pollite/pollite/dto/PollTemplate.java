package com.pollite.pollite.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PollTemplate {
    @NotBlank
    private String text;

    @NotEmpty
    private List<String> answers;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;
}
