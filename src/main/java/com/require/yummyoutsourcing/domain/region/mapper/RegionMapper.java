package com.require.yummyoutsourcing.domain.region.mapper;

import com.require.yummyoutsourcing.domain.region.dto.RegionResponseDto;
import com.require.yummyoutsourcing.domain.region.model.Region;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RegionMapper {

    /**
     * Region 엔티티를 RegionResponseDto로 변환합니다.
     *
     * @param region 변환할 Region 엔티티
     * @return 변환된 RegionResponseDto 객체
     */
    public RegionResponseDto toDto(Region region) {
        return RegionResponseDto.builder()
                .regionId(region.getId())
                .regionCode(region.getRegionCode())
                .regionName(region.getRegionName())
                .build();
    }

    /**
     * Region 엔티티 리스트를 RegionResponseDto 리스트로 변환합니다.
     *
     * @param regions 변환할 Region 엔티티 리스트
     * @return 변환된 RegionResponseDto 리스트
     */
    public List<RegionResponseDto> toDtoList(List<Region> regions) {
        return regions.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}