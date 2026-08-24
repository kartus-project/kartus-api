package com.kartus.api.domain.room.entity;

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
}
