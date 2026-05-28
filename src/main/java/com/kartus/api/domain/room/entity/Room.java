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

    @TimeToLive
    private Long ttl;

    public Room() {}

    public Room(String id, Long owner, String title, Short maxPlayer) {
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.maxPlayer = maxPlayer;
        this.currentPlayer = 0;
        this.ttl = null;
    }
}
