package com.kartus.api.domain.ticket.dto;

public record TicketPayload(
        Long userId,
        String roomId
) {
}
