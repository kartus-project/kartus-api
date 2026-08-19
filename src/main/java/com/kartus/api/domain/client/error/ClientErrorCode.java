package com.kartus.api.domain.client.error;

import com.kartus.api.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ClientErrorCode implements ErrorCode {
    MANIFEST_NOT_READABLE(HttpStatus.INTERNAL_SERVER_ERROR, "클라이언트 매니페스트를 읽을 수 없습니다."),
    MANIFEST_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, "클라이언트 매니페스트가 올바르지 않습니다."),
    PLATFORM_NOT_AVAILABLE(HttpStatus.NOT_FOUND, "해당 플랫폼의 배포 파일이 없습니다."),
    CLIENT_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "클라이언트 배포 파일을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String defaultMessage;

    ClientErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }
}
