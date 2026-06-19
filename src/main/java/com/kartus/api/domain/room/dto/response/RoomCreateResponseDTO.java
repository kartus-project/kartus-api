package com.kartus.api.domain.room.dto.response;

public record RoomCreateResponseDTO(
        String roomId,
        String title,
        Short maxPlayer,
        Long trackId
) {
}
