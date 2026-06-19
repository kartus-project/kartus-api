package com.kartus.api.domain.room.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class RoomMemberRepository {
    private final StringRedisTemplate stringRedisTemplate;

    private String roomKey(String roomId) {
        return "room:" + roomId + ":members";
    }

    private String userKey(String userId) {
        return "user:" + userId + ":rooms";
    }

    public void join(String roomId, String userId) {
        stringRedisTemplate.executePipelined((RedisCallback<Object>) conn -> {
            conn.sAdd(roomKey(roomId).getBytes(), userId.getBytes());
            conn.sAdd(userKey(userId).getBytes(), roomId.getBytes());
            return null;
        });
    }

    public void leave(String roomId, String userId) {
        stringRedisTemplate.executePipelined((RedisCallback<Object>) conn -> {
            conn.sRem(roomKey(roomId).getBytes(), userId.getBytes());
            conn.sRem(userKey(userId).getBytes(), roomId.getBytes());
            return null;
        });
    }

    public boolean isMember(String roomId, String userId) {
        return stringRedisTemplate.opsForSet().isMember(roomKey(roomId), userId);
    }

    public Set<String> getMembers(String roomId) {
        return stringRedisTemplate.opsForSet().members(roomKey(roomId));
    }

    public long count(String roomId) {
        Long size = stringRedisTemplate.opsForSet().size(roomKey(roomId));
        return size == null ? 0 : size;
    }

    // 고아 발생 위험 있음
    // RedisTemplate raw DEL을 MULTI/EXEC로 묶어서 방지 가능
    public void deleteRoom(String roomId) {
        stringRedisTemplate.delete(roomKey(roomId));
    }
}
