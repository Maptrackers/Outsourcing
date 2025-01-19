package com.require.yummyoutsourcing.common;


import org.springframework.data.domain.PageRequest;

/**
 * 페이지네이션 요청을 생성하는 유틸리티 클래스
 */
public class PaginationUtils {

    /**
     * 페이지 요청 생성
     * @param page 페이지 번호 (1부터 시작)
     * @param size 페이지 크기
     * @return PageRequest 객체
     */
    public static PageRequest createPageRequest(int page, int size) {
        return PageRequest.of(adjustPageIndex(page), validateSize(size));
    }

    // 1부터 시작하는 페이지 번호를 0부터 시작하도록 조정
    private static int adjustPageIndex(int page) {
        if (page <= 0) throw new IllegalArgumentException("페이지 번호는 1 이상이어야 합니다.");
        return page - 1;
    }

    // 페이지 크기를 검증 및 반환
    private static int validateSize(int size) {
        if (size <= 0) throw new IllegalArgumentException("페이지 크기는 1 이상이어야 합니다.");
        return size;
    }
}