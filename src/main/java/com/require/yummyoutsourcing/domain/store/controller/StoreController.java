package com.require.yummyoutsourcing.domain.store.controller;

import com.require.yummyoutsourcing.common.ApiResponse;
import com.require.yummyoutsourcing.domain.store.dto.StoreRequestDto;
import com.require.yummyoutsourcing.domain.store.dto.StoreResponseDto;
import com.require.yummyoutsourcing.domain.store.model.Category;
import com.require.yummyoutsourcing.domain.store.service.CreateStoreService;
import com.require.yummyoutsourcing.domain.store.service.DeleteStoreService;
import com.require.yummyoutsourcing.domain.store.service.ReadStoreService;
import com.require.yummyoutsourcing.domain.store.service.UpdateStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/store")
@RequiredArgsConstructor
public class StoreController {

    private final CreateStoreService createStoreService;
    private final ReadStoreService readStoreService;
    private final UpdateStoreService updateStoreService;
    private final DeleteStoreService deleteStoreService;

    /**
     * 새로운 가게를 등록합니다.
     *
     * @param request 가게 등록 요청 데이터
     * @return 등록된 가게 정보와 성공 메시지
     */
    @PostMapping
    public ResponseEntity<ApiResponse<StoreResponseDto>> createNewStore(@RequestBody StoreRequestDto request) {
        ApiResponse<StoreResponseDto> response = createStoreService.createStore(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 가게의 정보를 조회합니다.
     *
     * @param storeId 조회할 가게 ID
     * @return 가게 정보와 성공 메시지
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResponse<StoreResponseDto>> fetchStoreById(@PathVariable(name = "storeId") Long storeId) {
        ApiResponse<StoreResponseDto> response = readStoreService.getStoreById(storeId);
        return ResponseEntity.ok(response);
    }

    /**
     * 여러 가게의 목록을 조건에 따라 조회합니다.
     *
     * @param category   카테고리 필터 (선택)
     * @param regionId 지역 코드 필터 (선택)
     * @param name       가게 이름 필터 (선택)
     * @param page       페이지 번호 (기본값: 1)
     * @param size       페이지 크기 (기본값: 10)
     * @return 가게 목록 및 페이지 정보
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getStoresByFilters(
            @RequestParam(name = "category", required = false) Category category,
            @RequestParam(name = "regionId", required = false) Long regionId,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        ApiResponse<Object> response = readStoreService.getFilteredStoreList(category, regionId, name, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 가게의 정보를 수정합니다.
     *
     * @param storeId 수정할 가게 ID
     * @param request 가게 수정 요청 데이터
     * @return 수정된 가게 정보와 성공 메시지
     */
    @PatchMapping("/{storeId}")
    public ResponseEntity<ApiResponse<StoreResponseDto>> updateStoreDetails(
            @PathVariable("storeId") Long storeId,
            @RequestBody StoreRequestDto request) {
        ApiResponse<StoreResponseDto> response = updateStoreService.updateStore(storeId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 가게를 삭제합니다.
     *
     * @param storeId 삭제할 가게 ID
     * @return 삭제 성공 메시지
     */
    @DeleteMapping("/{storeId}")
    public ResponseEntity<ApiResponse<Object>> removeStore(@PathVariable("storeId") Long storeId) {
        ApiResponse<Object> response = deleteStoreService.deleteStore(storeId);
        return ResponseEntity.ok(response);
    }
}