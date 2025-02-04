package com.require.yummyoutsourcing.domain.region.controller;

import com.require.yummyoutsourcing.common.ApiResponse;
import com.require.yummyoutsourcing.domain.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    /**
     * 지역 코드 접두사로 필터링하여 지역 목록을 페이지 단위로 조회합니다.
     *
     * @param regionCode 지역 코드를 필터링하기 위한 접두사
     * @param page       조회할 페이지 번호 (1부터 시작, 기본값: 1)
     * @param size       페이지당 항목 수 (기본값: 10)
     * @return 지역 목록과 페이지네이션 정보를 포함한 ApiResponse 객체
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> fetchRegionsByPrefix(
            @RequestParam(name = "regionCode") String regionCode,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        ApiResponse<Object> response = regionService.getRegions(regionCode, page, size);
        return ResponseEntity.ok(response);
    }
}
