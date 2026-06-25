package com.kartus.api.domain.room.event;

import java.time.Instant;

public sealed interface RoomEvent permits RoomJoinedEvent, RoomLeftEvent, RoomTrackChangedEvent {
    RoomEventType eventType();

    String roomId();

    Instant occurredAt();
}
