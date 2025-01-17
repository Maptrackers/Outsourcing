package com.require.yummyoutsourcing.domain.store.dto;

import com.require.yummyoutsourcing.domain.store.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponseDto {
    private Long id;
    private String name;
    private Category category;
    private Long regionId;
}
