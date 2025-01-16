package com.require.yummyoutsourcing.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 전역 예외 처리 클래스.
 * 컨트롤러에서 발생하는 예외를 한 곳에서 처리하여 응답 형태를 일관되게 유지합니다.
 */
@ControllerAdvice
public class AopiGlobalExceptionHandler {

    /**
     * IllegalArgumentException 예외를 처리합니다.
     *
     * @param ex 처리할 IllegalArgumentException 객체
     * @return HTTP 400 Bad Request 상태와 함께 ApiResponse 객체를 반환
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse<Object> errorResponse = ApiResponse.builder()
                .data(-1)
                .message(ex.getMessage())
                .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }
}