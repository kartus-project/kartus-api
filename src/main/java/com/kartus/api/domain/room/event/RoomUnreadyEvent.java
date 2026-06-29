package com.kartus.api.domain.room.event;

import java.time.Instant;

public record RoomUnreadyEvent(
        RoomEventType eventType,
        String roomId,
        Long userId,
        Instant occurredAt
) implements RoomEvent {

    public static RoomUnreadyEvent of(String roomId, Long userId) {
        return new RoomUnreadyEvent(RoomEventType.ROOM_UNREADY, roomId, userId, Instant.now());
    }
}
