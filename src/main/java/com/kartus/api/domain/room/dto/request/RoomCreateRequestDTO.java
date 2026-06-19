package com.kartus.api.domain.room.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomCreateRequestDTO(
        @NotBlank
        String title,

        @Min(1)
        @Max(4)
        @NotNull
        Short maxPlayer
) {
}
