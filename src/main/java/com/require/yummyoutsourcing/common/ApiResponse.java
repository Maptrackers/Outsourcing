package com.require.yummyoutsourcing.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * API 응답을 캡슐화하는 공통 클래스.
 * 모든 API 응답은 이 클래스를 사용하여 표준화된 형식으로 반환됩니다.
 *
 * @param <T> 응답 데이터의 타입
 */
@Data
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private Map<String, Object> pageable;
    private String message;
}
