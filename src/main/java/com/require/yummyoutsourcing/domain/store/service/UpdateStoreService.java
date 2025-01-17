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

    public ApiResponse<StoreResponseDto> updateStore(Long storeId, StoreRequestDto request) {
        Store existingStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게 정보 수정 실패"));
        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("지역 정보를 찾을 수 없습니다."));

        existingStore.update(request.getName(), request.getCategory(), region);

        Store savedStore = storeRepository.save(existingStore);

        return ApiResponse.<StoreResponseDto>builder()
                .data(storeMapper.toDto(savedStore))
                .message("가게 정보 수정 완료")
                .build();
    }
}
