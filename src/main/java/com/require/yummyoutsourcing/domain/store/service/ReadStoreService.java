package com.require.yummyoutsourcing.domain.store.service;

import com.require.yummyoutsourcing.common.ApiResponse;
import com.require.yummyoutsourcing.domain.store.dto.StoreResponseDto;
import com.require.yummyoutsourcing.domain.store.mapper.StoreMapper;
import com.require.yummyoutsourcing.domain.store.model.Category;
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
     * ID를 기반으로 특정 가게를 조회합니다.
     *
     * @param storeId 조회할 가게의 ID
     * @return 가게의 상세 정보와 성공 메시지를 포함한 ApiResponse
     */
    public ApiResponse<StoreResponseDto> getStoreById(Long storeId) {
        Store store = fetchStoreById(storeId);
        StoreResponseDto storeDetails = convertToStoreDto(store);
        return buildSingleStoreResponse(storeDetails);
    }

    /**
     * 필터링 및 페이지네이션 조건으로 가게 목록을 조회합니다.
     *
     * @param category   필터링할 카테고리 (옵션)
     * @param regionId 필터링할 지역 코드 (옵션)
     * @param name       필터링할 가게 이름 (옵션)
     * @param page       조회할 페이지 번호 (1부터 시작)
     * @param size       페이지당 항목 수
     * @return 가게 목록, 페이지 정보와 성공 메시지를 포함한 ApiResponse
     */
    public ApiResponse<Object> getFilteredStoreList(Category category, Long regionId, String name, int page, int size) {
        PageRequest pageable = createPageRequest(page, size);
        Page<Store> storePage = fetchFilteredStores(category, regionId, name, pageable);

        validateStoreList(storePage);

        List<StoreResponseDto> storeList = convertToStoreDtoList(storePage);
        Map<String, Object> paginationInfo = generatePaginationMetadata(page, size, storePage);

        return buildStoreListResponse(storeList, paginationInfo);
    }

    // ID로 가게 엔티티 조회
    private Store fetchStoreById(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게가 존재하지 않습니다."));
    }

    // 단일 가게 DTO 변환
    private StoreResponseDto convertToStoreDto(Store store) {
        return storeMapper.toDto(store);
    }

    // 단일 가게 응답 생성
    private ApiResponse<StoreResponseDto> buildSingleStoreResponse(StoreResponseDto storeDetails) {
        return ApiResponse.<StoreResponseDto>builder()
                .data(storeDetails)
                .message("가게 조회 성공")
                .build();
    }

    // 페이지 요청 생성
    private PageRequest createPageRequest(int page, int size) {
        return PageRequest.of(page - 1, size);
    }

    // 필터 조건으로 가게 목록 조회
    private Page<Store> fetchFilteredStores(Category category, Long regionId, String name, PageRequest pageable) {
        return storeRepository.findByFilters(category, regionId, name, pageable);
    }

    // 가게 목록 결과 검증
    private void validateStoreList(Page<Store> storePage) {
        if (storePage.isEmpty())
            throw new IllegalArgumentException("조회된 가게 목록이 없습니다.");
    }

    // 가게 목록 DTO 변환
    private List<StoreResponseDto> convertToStoreDtoList(Page<Store> storePage) {
        return storeMapper.toDtoList(storePage.getContent());
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

    // 가게 목록 응답 생성
    private ApiResponse<Object> buildStoreListResponse(List<StoreResponseDto> storeList, Map<String, Object> paginationInfo) {
        return ApiResponse.builder()
                .data(Map.of(
                        "stores", storeList,
                        "pageable", paginationInfo
                ))
                .message("가게 목록 조회 성공")
                .build();
    }
}