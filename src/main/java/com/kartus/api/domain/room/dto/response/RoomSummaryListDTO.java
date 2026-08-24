package com.kartus.api.domain.room.dto.response;

import java.util.List;

public record RoomSummaryListDTO(
        List<RoomSummaryDTO> rooms
) {
}
