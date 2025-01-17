package com.require.yummyoutsourcing.domain.store.service;

import com.require.yummyoutsourcing.common.ApiResponse;
import com.require.yummyoutsourcing.domain.region.model.Region;
import com.require.yummyoutsourcing.domain.region.repository.RegionRepository;
import com.require.yummyoutsourcing.domain.store.dto.StoreRequestDto;
import com.require.yummyoutsourcing.domain.store.dto.StoreResponseDto;
import com.require.yummyoutsourcing.domain.store.mapper.StoreMapper;
import com.require.yummyoutsourcing.domain.store.model.Category;
import com.require.yummyoutsourcing.domain.store.model.Store;
import com.require.yummyoutsourcing.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateStoreServiceTest {

    @InjectMocks
    private CreateStoreService createStoreService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private StoreMapper storeMapper;

    @Test
    void test() {
        Region mockRegion = Region.builder()
                .id(1L)
                .regionCode("12345")
                .regionName("Sample Region")
                .build();

        StoreRequestDto requestDto = StoreRequestDto.builder()
                .name("맛집가게")
                .category(Category.CHICKEN)
                .regionId(mockRegion.getId())
                .build();

        Store mockStore = Store.builder()
                .id(1L)
                .name("맛집가게")
                .category(Category.CHICKEN)
                .region(mockRegion)
                .build();

        StoreResponseDto responseDto = StoreResponseDto.builder()
                .id(1L)
                .name("맛집가게")
                .category(Category.CHICKEN)
                .regionId(mockRegion.getId())
                .build();

        when(regionRepository.findById(1L)).thenReturn(Optional.of(mockRegion));
        when(storeMapper.toEntity(requestDto, mockRegion)).thenReturn(mockStore);
        when(storeRepository.save(mockStore)).thenReturn(mockStore);
        when(storeMapper.toDto(mockStore)).thenReturn(responseDto);

        ApiResponse<StoreResponseDto> response = createStoreService.createStore(requestDto);

        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("가게 등록 성공");
    }
}
