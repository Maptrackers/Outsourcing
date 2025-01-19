package com.require.yummyoutsourcing.domain.store.service;

import com.require.yummyoutsourcing.common.ApiResponse;
import com.require.yummyoutsourcing.domain.region.model.Region;
import com.require.yummyoutsourcing.domain.region.repository.RegionRepository;
import com.require.yummyoutsourcing.domain.store.dto.StoreRequestDto;
import com.require.yummyoutsourcing.domain.store.dto.StoreResponseDto;
import com.require.yummyoutsourcing.domain.store.mapper.StoreMapper;
import com.require.yummyoutsourcing.domain.store.model.Store;
import com.require.yummyoutsourcing.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateStoreService {

    private final RegionRepository regionRepository;
    private final StoreMapper storeMapper;
    private final StoreRepository storeRepository;

    /**
     * 새로운 가게를 등록합니다.
     *
     * @param request 가게 등록 요청 DTO
     * @return 등록된 가게의 정보가 포함된 ApiResponse
     */
    public ApiResponse<StoreResponseDto> createStore(StoreRequestDto request) {
        Region region = findRegionById(request.getRegionId());
        Store store = mapToEntityAndSave(request, region);
        StoreResponseDto response = mapToResponse(store);

        return buildSuccessResponse(response);
    }

    // 주어진 ID로 Region을 조회하거나 예외를 발생시킴
    private Region findRegionById(Long regionId) {
        return regionRepository.findById(regionId)
                .orElseThrow(() -> new IllegalArgumentException("가게 등록 실패"));
    }

    // StoreRequestDto와 Region으로 Store 엔티티를 생성 및 저장
    private Store mapToEntityAndSave(StoreRequestDto request, Region region) {
        Store store = storeMapper.toEntity(request, region);
        return storeRepository.save(store);
    }

    // Store 엔티티를 StoreResponseDto로 변환
    private StoreResponseDto mapToResponse(Store store) {
        return storeMapper.toDto(store);
    }

    // 성공 응답을 생성
    private ApiResponse<StoreResponseDto> buildSuccessResponse(StoreResponseDto response) {
        return ApiResponse.<StoreResponseDto>builder()
                .data(response)
                .message("가게 등록 성공")
                .build();
    }
}