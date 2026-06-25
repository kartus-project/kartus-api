package com.kartus.api.domain.room.event;

import java.time.Instant;

public record RoomJoinedEvent(
        RoomEventType eventType,
        String roomId,
        Long userId,
        Instant occurredAt
) implements RoomEvent {

    public static RoomJoinedEvent of(String roomId, Long userId) {
        return new RoomJoinedEvent(RoomEventType.ROOM_JOINED, roomId, userId, Instant.now());
    }
}
