package com.kartus.api.domain.room.error;

import com.kartus.api.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum RoomErrorCode implements ErrorCode {
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "방을 찾을 수 없습니다."),
    ROOM_FULL(HttpStatus.CONFLICT, "방이 가득 찼습니다."),
    ALREADY_JOINED(HttpStatus.CONFLICT, "이미 참가한 방입니다."),
    NOT_A_MEMBER(HttpStatus.CONFLICT, "방에 참가하고 있지 않습니다."),
    NOT_ROOM_OWNER(HttpStatus.FORBIDDEN, "방장만 변경할 수 있습니다.");

    private final HttpStatus status;
    private final String defaultMessage;

    RoomErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }
}
