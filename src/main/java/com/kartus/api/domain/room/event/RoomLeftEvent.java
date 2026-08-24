package com.kartus.api.domain.room.event;

import java.time.Instant;

public record RoomLeftEvent(
        RoomEventType eventType,
        String roomId,
        Long userId,
        Instant occurredAt
) implements RoomEvent {

    public static RoomLeftEvent of(String roomId, Long userId) {
        return new RoomLeftEvent(RoomEventType.ROOM_LEFT, roomId, userId, Instant.now());
    }
}
