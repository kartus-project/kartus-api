package com.kartus.api.domain.room.dto.response;

public record RoomMemberDTO(
        Long userId,
        String nickname,
        boolean isReady
) {
}
