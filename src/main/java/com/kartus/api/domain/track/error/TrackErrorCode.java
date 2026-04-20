package com.kartus.api.domain.track.error;

import com.kartus.api.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TrackErrorCode implements ErrorCode {
    TRACK_NOT_FOUND(HttpStatus.NOT_FOUND, "Track not found");

    private final HttpStatus status;
    private final String defaultMessage;

    TrackErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }
}
