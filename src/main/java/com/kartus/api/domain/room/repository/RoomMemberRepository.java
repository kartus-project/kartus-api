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

    public boolean isInAnyRoom(String userId) {
        Long size = stringRedisTemplate.opsForSet().size(userKey(userId));
        return size != null && size > 0;
    }

    public Set<String> getMembers(String roomId) {
        return stringRedisTemplate.opsForSet().members(roomKey(roomId));
    }

    public boolean areAllMembersReady(String roomId) {
        if (count(roomId) <= 0) {
            return false;
        }

        Set<String> notReadyMembers = stringRedisTemplate.opsForSet()
                .difference(roomKey(roomId), readyKey(roomId));
        return notReadyMembers != null && notReadyMembers.isEmpty();
    }

    public long count(String roomId) {
        Long size = stringRedisTemplate.opsForSet().size(roomKey(roomId));
        return size == null ? 0 : size;
    }

    public void deleteRoom(String roomId) {
        Set<String> memberIds = getMembers(roomId);

        stringRedisTemplate.executePipelined((RedisCallback<Object>) conn -> {
            if (memberIds != null) {
                for (String memberId : memberIds) {
                    conn.sRem(userKey(memberId).getBytes(), roomId.getBytes());
                }
            }
            conn.del(roomKey(roomId).getBytes());
            conn.del(readyKey(roomId).getBytes());
            return null;
        });
    }
}
