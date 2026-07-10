package com.kartus.api.domain.room.event;

import java.time.Instant;

public record RoomGameStartedEvent(
        RoomEventType eventType,
        String roomId,
        Long userId,
        Instant occurredAt
) implements RoomEvent {

    public static RoomGameStartedEvent of(String roomId, Long userId) {
        return new RoomGameStartedEvent(RoomEventType.ROOM_GAME_STARTED, roomId, userId, Instant.now());
    }
}
