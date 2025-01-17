package com.require.yummyoutsourcing.domain.store.service;

import com.require.yummyoutsourcing.common.ApiResponse;
import com.require.yummyoutsourcing.domain.region.dto.RegionResponseDto;
import com.require.yummyoutsourcing.domain.region.model.Region;
import com.require.yummyoutsourcing.domain.region.repository.RegionRepository;
import com.require.yummyoutsourcing.domain.store.dto.StoreRequestDto;
import com.require.yummyoutsourcing.domain.store.dto.StoreResponseDto;
import com.require.yummyoutsourcing.domain.store.mapper.StoreMapper;
import com.require.yummyoutsourcing.domain.store.model.Store;
import com.require.yummyoutsourcing.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CreateStoreService {

    private final RegionRepository regionRepository;
    private final StoreMapper storeMapper;
    private final StoreRepository storeRepository;

    public ApiResponse<StoreResponseDto> createStore(StoreRequestDto request) {
        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("가게 등록 실패"));

        Store store = storeMapper.toEntity(request, region);
        Store savedStore = storeRepository.save(store);

        StoreResponseDto response = storeMapper.toDto(savedStore);

        return ApiResponse.<StoreResponseDto>builder()
                .data(response)
                .message("가게 등록 성공")
                .build();
    }
}
