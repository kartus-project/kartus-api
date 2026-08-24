package com.kartus.api.domain.ticket.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TicketConsumeRequestDTO(
        @NotBlank
        String ticket
) {
}
