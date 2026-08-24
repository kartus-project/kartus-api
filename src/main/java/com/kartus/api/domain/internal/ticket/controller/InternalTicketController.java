package com.kartus.api.domain.internal.ticket.controller;

import com.kartus.api.domain.ticket.dto.TicketPayload;
import com.kartus.api.domain.ticket.dto.request.TicketConsumeRequestDTO;
import com.kartus.api.domain.ticket.dto.response.TicketConsumeResponseDTO;
import com.kartus.api.domain.ticket.service.TicketService;
import com.kartus.api.global.dto.GlobalApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("internal/ticket")
@RequiredArgsConstructor
public class InternalTicketController {
    private final TicketService ticketService;

    @PostMapping("consume")
    public ResponseEntity<GlobalApiResponse<TicketConsumeResponseDTO>> consume(
            @Valid @RequestBody TicketConsumeRequestDTO dto
    ) {
        TicketPayload payload = ticketService.consume(dto.ticket());

        return ResponseEntity.ok(GlobalApiResponse.success(
                new TicketConsumeResponseDTO(payload.userId(), payload.roomId())));
    }
}
