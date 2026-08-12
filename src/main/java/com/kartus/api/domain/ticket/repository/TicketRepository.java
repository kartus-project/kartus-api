package com.kartus.api.domain.ticket.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class TicketRepository {
    private final StringRedisTemplate stringRedisTemplate;

    private String ticketKey(String ticketId) {
        return "ticket:" + ticketId;
    }

    public void save(String ticketId, String payload, Duration ttl) {
        stringRedisTemplate.opsForValue().set(ticketKey(ticketId), payload, ttl);
    }

    public String getAndDelete(String ticketId) {
        return stringRedisTemplate.opsForValue().getAndDelete(ticketKey(ticketId));
    }
}
