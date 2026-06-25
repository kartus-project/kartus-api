package com.kartus.api.domain.room.event;

import java.time.Instant;

public record RoomTrackChangedEvent(
        RoomEventType eventType,
        String roomId,
        Long trackId,
        Long userId,
        Instant occurredAt
) implements RoomEvent {

    public static RoomTrackChangedEvent of(String roomId, Long trackId, Long userId) {
        return new RoomTrackChangedEvent(RoomEventType.ROOM_TRACK_CHANGED, roomId, trackId, userId, Instant.now());
    }
}
