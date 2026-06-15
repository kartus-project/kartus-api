package com.kartus.api.domain.room.dto.response;

public record RoomSummaryDTO(
        String roomId,
        String title,
        Short currentPlayer,
        Short maxPlayer,
        Long trackId
) {
}
