package com.kartus.api.domain.room.dto.response;

import java.util.List;

public record RoomDetailResponseDTO(
        String roomId,
        String title,
        Short currentPlayer,
        Short maxPlayer,
        Long trackId,
        String trackName,
        List<RoomMemberDTO> members
) {
}
