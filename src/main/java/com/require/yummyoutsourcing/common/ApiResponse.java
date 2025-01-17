package com.require.yummyoutsourcing.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * API 응답을 캡슐화하는 공통 클래스.
 * 모든 API 응답은 이 클래스를 사용하여 표준화된 형식으로 반환됩니다.
 *
 * @param <T> 응답 데이터의 타입
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private T data;
    private Map<String, Object> pageable;
    private String message;
}
