package com.kartus.api.domain.ticket.error;

import com.kartus.api.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TicketErrorCode implements ErrorCode {
    TICKET_NOT_FOUND(HttpStatus.NOT_FOUND, "유효하지 않은 티켓입니다.");

    private final HttpStatus status;
    private final String defaultMessage;

    TicketErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }
}
