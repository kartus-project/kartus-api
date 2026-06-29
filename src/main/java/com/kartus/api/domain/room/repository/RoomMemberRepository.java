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

    private String readyKey(String roomId) {
        return "room:" + roomId + ":ready";
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
            conn.sRem(readyKey(roomId).getBytes(), userId.getBytes());
            return null;
        });
    }

    public boolean addReady(String roomId, String userId) {
        Long added = stringRedisTemplate.opsForSet().add(readyKey(roomId), userId);
        return added != null && added > 0;
    }

    public boolean removeReady(String roomId, String userId) {
        Long removed = stringRedisTemplate.opsForSet().remove(readyKey(roomId), userId);
        return removed != null && removed > 0;
    }

    public Set<String> getReadyMembers(String roomId) {
        return stringRedisTemplate.opsForSet().members(readyKey(roomId));
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
        stringRedisTemplate.delete(readyKey(roomId));
    }
}
