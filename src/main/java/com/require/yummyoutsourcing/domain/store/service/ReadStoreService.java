package com.require.yummyoutsourcing.domain.store.service;

import com.require.yummyoutsourcing.common.ApiResponse;
import com.require.yummyoutsourcing.domain.store.dto.StoreResponseDto;
import com.require.yummyoutsourcing.domain.store.mapper.StoreMapper;
import com.require.yummyoutsourcing.domain.store.model.Store;
import com.require.yummyoutsourcing.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReadStoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    /**
     * 특정 가게를 ID로 조회합니다.
     *
     * @param storeId 조회할 가게의 ID
     * @return 조회된 가게의 상세 정보
     */
    public ApiResponse<StoreResponseDto> readStore(Long storeId) {
        Store store = findStoreById(storeId);
        StoreResponseDto response = storeMapper.toDto(store);

        return createSingleStoreResponse(response);
    }

    /**
     * 페이지 정보에 따라 필터링된 가게 목록을 조회합니다.
     *
     * @param category   필터링할 카테고리 (옵션)
     * @param regionCode 필터링할 지역 코드 (옵션)
     * @param name       필터링할 이름 (옵션)
     * @param page       조회할 페이지 번호 (1부터 시작)
     * @param size       페이지당 항목 수
     * @return 가게 목록과 페이지네이션 정보를 포함한 ApiResponse 객체
     */
    public ApiResponse<Object> readAllStores(String category, String regionCode, String name, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);

        Page<Store> storePage = storeRepository.findByFilters(category, regionCode, name, pageable);

        if (storePage.isEmpty())
            throw new IllegalArgumentException("가게 목록이 존재하지 않습니다.");

        List<StoreResponseDto> storeDtoList = storeMapper.toDtoList(storePage.getContent());

        Map<String, Object> pageableInfo = generatePaginationMetadata(page, size, storePage);

        return ApiResponse.builder()
                .data(Map.of(
                        "stores", storeDtoList,
                        "pageable", pageableInfo
                ))
                .message("가게 목록 조회 성공")
                .build();
    }

    // 페이지네이션 메타데이터 생성
    private Map<String, Object> generatePaginationMetadata(int page, int size, Page<Store> storePage) {
        return Map.of(
                "page", page,
                "size", size,
                "totalPages", storePage.getTotalPages(),
                "totalItems", storePage.getTotalElements()
        );
    }

    // 단일 가게 응답 생성
    private ApiResponse<StoreResponseDto> createSingleStoreResponse(StoreResponseDto response) {
        return ApiResponse.<StoreResponseDto>builder()
                .data(response)
                .message("가게 조회 성공")
                .build();
    }

    // ID로 가게 엔티티 조회
    private Store findStoreById(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게 조회 실패"));
    }
}