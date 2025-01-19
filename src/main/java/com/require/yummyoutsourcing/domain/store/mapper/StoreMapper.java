package com.require.yummyoutsourcing.domain.store.mapper;

import com.require.yummyoutsourcing.domain.region.model.Region;
import com.require.yummyoutsourcing.domain.store.dto.StoreRequestDto;
import com.require.yummyoutsourcing.domain.store.dto.StoreResponseDto;
import com.require.yummyoutsourcing.domain.store.model.Store;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StoreMapper {

    /**
     * StoreRequestDto와 Region을 기반으로 Store 엔티티 생성
     *
     * @param dto    가게 요청 DTO
     * @param region 지역 엔티티
     * @return 변환된 Store 엔티티
     */
    public Store toEntity(StoreRequestDto dto, Region region) {
        return Store.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .region(region)
                .build();
    }

    /**
     * Store 엔티티를 StoreResponseDto로 변환
     *
     * @param store 가게 엔티티
     * @return 변환된 Store 응답 DTO
     */
    public StoreResponseDto toDto(Store store) {
        return StoreResponseDto.builder()
                .id(store.getId())
                .name(store.getName())
                .category(store.getCategory())
                .regionId(store.getRegion().getId())
                .build();
    }

    /**
     * Store 엔티티 리스트를 StoreResponseDto 리스트로 변환
     *
     * @param stores 가게 엔티티 리스트
     * @return 변환된 Store 응답 DTO 리스트
     */
    public List<StoreResponseDto> toDtoList(List<Store> stores) {
        return stores.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
