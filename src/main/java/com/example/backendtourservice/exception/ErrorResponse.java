package com.example.backendtourservice.exception;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int statusCode;
    private final String error;
    private final String message;
    // 선택적으로 추가 메시지를 보내고 싶을 경우
    private String customMessage;

    public ErrorResponse(ErrorCode errorCode) {
        this.statusCode = errorCode.getHttpStatus().value();
        this.error = errorCode.getHttpStatus().name();
        this.message = errorCode.getMessage();
    }

    public ErrorResponse(ErrorCode errorCode, String customMessage) {
        this.statusCode = errorCode.getHttpStatus().value();
        this.error = errorCode.getHttpStatus().name();
        this.message = errorCode.getMessage();
        this.customMessage = customMessage;
    }
}
