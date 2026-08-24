package com.kartus.api.domain.room.event;

import java.time.Instant;

public record RoomOwnerChangedEvent(
        RoomEventType eventType,
        String roomId,
        Long previousOwnerId,
        Long newOwnerId,
        Instant occurredAt
) implements RoomEvent {

    public static RoomOwnerChangedEvent of(String roomId, Long previousOwnerId, Long newOwnerId) {
        return new RoomOwnerChangedEvent(RoomEventType.ROOM_OWNER_CHANGED, roomId, previousOwnerId, newOwnerId, Instant.now());
    }
}
