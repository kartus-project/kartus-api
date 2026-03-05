package com.kartus.api.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getDefaultMessage();
    HttpStatus getStatus();
}
