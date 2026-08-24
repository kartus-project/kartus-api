package com.kartus.api.domain.room.service;

import com.kartus.api.domain.room.event.RoomEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisStreamCommands.XAddOptions;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomEventPublisher {
    private static final String STREAM_KEY = "room:events";

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${room.event.stream.max-len:10000}")
    private long maxLen;

    public void publish(RoomEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            MapRecord<String, String, String> record = StreamRecords.newRecord()
                    .in(STREAM_KEY)
                    .ofMap(Map.of("payload", json));
            XAddOptions options = XAddOptions.maxlen(maxLen).approximateTrimming(true);
            stringRedisTemplate.opsForStream().add(record, options);
        } catch (Exception e) {
            log.error("Failed to publish room event. eventType={}, roomId={}",
                    event.eventType(), event.roomId(), e);
        }
    }
}
