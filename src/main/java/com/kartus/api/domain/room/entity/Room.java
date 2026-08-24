package com.kartus.api.domain.room.entity;

import com.kartus.api.domain.room.enums.RoomStatus;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("room")
@Getter
public class Room {
    @Id
    private String id;
    private Long owner;
    private String title;
    private Short maxPlayer;
    private Short currentPlayer;
    private Long trackId;
    private RoomStatus status;

    @TimeToLive
    private Long ttl;

    private Room() {}

    public static Room create(String id, Long owner, String title, Short maxPlayer, Long trackId) {
        Room room = new Room();
        room.id = id;
        room.owner = owner;
        room.title = title;
        room.maxPlayer = maxPlayer;
        room.trackId = trackId;
        room.currentPlayer = 0;
        room.status = RoomStatus.WAITING;
        room.ttl = null;
        return room;
    }

    public void syncPlayerCount(long memberCount) {
        this.currentPlayer = (short) memberCount;
    }

    public void changeOwner(Long newOwner) {
        this.owner = newOwner;
    }

    public void changeTrack(Long trackId) {
        this.trackId = trackId;
    }

    public void start() {
        this.status = RoomStatus.STARTED;
    }

    public boolean isStarted() {
        return this.status == RoomStatus.STARTED;
    }
}
