package com.require.yummyoutsourcing.domain.store.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreResponseDto {
    private Long id;
    private String name;
    private Enum category;
    private Long regionId;
}
