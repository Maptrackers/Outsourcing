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

    public Store toEntity(StoreRequestDto dto, Region region) {
        return Store.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .region(region)
                .build();
    }

    public StoreResponseDto toDto(Store store) {
        return StoreResponseDto.builder()
                .id(store.getId())
                .name(store.getName())
                .category(store.getCategory())
                .regionId(store.getRegion().getId())
                .build();
    }

    public List<StoreResponseDto> toDtoList(List<Store> stores) {
        return stores.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
