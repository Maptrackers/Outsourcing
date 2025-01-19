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
public class UpdateStoreService {

    private final StoreRepository storeRepository;
    private final RegionRepository regionRepository;
    private final StoreMapper storeMapper;

    /**
     * 가게 정보를 수정합니다.
     *
     * @param storeId 수정할 가게 ID
     * @param request 가게 수정 요청 데이터
     * @return 수정된 가게 정보와 성공 메시지를 포함한 ApiResponse
     */
    public ApiResponse<StoreResponseDto> updateStore(Long storeId, StoreRequestDto request) {
        Store existingStore = fetchExistingStore(storeId);
        Region region = fetchRegion(request.getRegionId());

        updateExistingStore(existingStore, request, region);
        Store savedStore = saveUpdatedStore(existingStore);

        return buildUpdateResponse(savedStore);
    }

    // 기존 가게 조회
    private Store fetchExistingStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게 정보를 찾을 수 없습니다."));
    }

    // 지역 정보 조회
    private Region fetchRegion(Long regionId) {
        return regionRepository.findById(regionId)
                .orElseThrow(() -> new IllegalArgumentException("지역 정보를 찾을 수 없습니다."));
    }

    // 기존 가게 정보 업데이트
    private void updateExistingStore(Store store, StoreRequestDto request, Region region) {
        store.update(request.getName(), request.getCategory(), region);
    }

    // 업데이트된 가게 정보 저장
    private Store saveUpdatedStore(Store store) {
        return storeRepository.save(store);
    }

    // ApiResponse 생성
    private ApiResponse<StoreResponseDto> buildUpdateResponse(Store savedStore) {
        return ApiResponse.<StoreResponseDto>builder()
                .data(storeMapper.toDto(savedStore))
                .message("가게 정보 수정 완료")
                .build();
    }
}