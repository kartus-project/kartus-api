package com.kartus.api.domain.room.dto.request;

import jakarta.validation.constraints.NotNull;

public record RoomTrackUpdateRequestDTO(
        @NotNull
        Long trackId
) {
}
