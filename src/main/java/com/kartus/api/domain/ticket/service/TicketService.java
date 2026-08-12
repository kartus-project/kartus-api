package com.kartus.api.domain.ticket.service;

import com.kartus.api.domain.ticket.dto.TicketPayload;
import com.kartus.api.domain.ticket.error.TicketErrorCode;
import com.kartus.api.domain.ticket.repository.TicketRepository;
import com.kartus.api.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {
    private static final Duration TICKET_TTL = Duration.ofSeconds(30);

    private final TicketRepository ticketRepository;
    private final ObjectMapper objectMapper;

    public String issue(Long userId, String roomId) {
        String ticketId = UUID.randomUUID().toString();
        String payload = objectMapper.writeValueAsString(new TicketPayload(userId, roomId));

        ticketRepository.save(ticketId, payload, TICKET_TTL);

        return ticketId;
    }

    public TicketPayload consume(String ticketId) {
        String payload = ticketRepository.getAndDelete(ticketId);

        if (payload == null) {
            throw new CustomException(TicketErrorCode.TICKET_NOT_FOUND);
        }

        return objectMapper.readValue(payload, TicketPayload.class);
    }
}
