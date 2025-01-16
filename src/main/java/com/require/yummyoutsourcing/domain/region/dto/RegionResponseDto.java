package com.require.yummyoutsourcing.domain.region.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionResponseDto {
    private Long regionId;
    private String regionCode;
    private String regionName;
}
