package com.kartus.api.domain.room.event;

import java.time.Instant;

public record RoomReadyEvent(
        RoomEventType eventType,
        String roomId,
        Long userId,
        Instant occurredAt
) implements RoomEvent {

    public static RoomReadyEvent of(String roomId, Long userId) {
        return new RoomReadyEvent(RoomEventType.ROOM_READY, roomId, userId, Instant.now());
    }
}
