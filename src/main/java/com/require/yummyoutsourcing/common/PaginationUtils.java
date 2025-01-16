package com.require.yummyoutsourcing.common;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

/**
 * 페이지네이션 처리를 위한 유틸리티 클래스.
 * Spring Data JPA의 PageRequest 객체 생성을 지원합니다.
 */
@NoArgsConstructor
public class PaginationUtils {

    /**
     * 기본 페이지네이션 처리.
     *
     * @param page 페이지 번호 (1부터 시작)
     * @param size 페이지 크기
     * @return PageRequest 객체
     */
    public static PageRequest createPageRequest(int page, int size) {
        validatePageAndSize(page, size);
        return PageRequest.of(page - 1, size);
    }

    //페이지와 크기 값의 유효성을 검증합니다.
    private static void validatePageAndSize(int page, int size) {
        if (page <= 0)
            throw new IllegalArgumentException("페이지 번호는 1 이상이어야 합니다.");
        if (size <= 0)
            throw new IllegalArgumentException("페이지 크기는 1 이상이어야 합니다.");
    }
}