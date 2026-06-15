package com.kartus.api.domain.room.dto.response;

import java.util.List;

public record RoomJoinResponseDTO(
        String roomId,
        String title,
        Short currentPlayer,
        Short maxPlayer,
        Long trackId,
        List<RoomMemberDTO> members
) {
}
