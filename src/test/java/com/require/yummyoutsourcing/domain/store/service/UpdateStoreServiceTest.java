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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateStoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private StoreMapper storeMapper;

    @InjectMocks
    private UpdateStoreService updateStoreService;

    @Test
    @DisplayName("가게 정보 수정 성공 테스트")
    void updateStore_Success() {
        // given
        Long storeId = 1L;
        Long regionId = 2L;
        StoreRequestDto request = new StoreRequestDto("Updated Store", Category.CHICKEN, regionId);
        Store existingStore = new Store(storeId, "Old Store", Category.PIZZA, new Region(regionId, "Region Code", "Region Name"));
        Region region = new Region(regionId, "Region Code", "Updated Region");
        Store updatedStore = new Store(storeId, "Updated Store", Category.CHICKEN, region);
        StoreResponseDto responseDto = new StoreResponseDto(storeId, "Updated Store", Category.CHICKEN, regionId);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(existingStore));
        when(regionRepository.findById(regionId)).thenReturn(Optional.of(region));
        when(storeRepository.save(any(Store.class))).thenReturn(updatedStore);
        when(storeMapper.toDto(any(Store.class))).thenReturn(responseDto);

        // when
        ApiResponse<StoreResponseDto> response = updateStoreService.updateStore(storeId, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("가게 정보 수정 완료");
        assertThat(response.getData().getName()).isEqualTo("Updated Store");
        verify(storeRepository).findById(storeId);
        verify(regionRepository).findById(regionId);
        verify(storeRepository).save(any(Store.class));
    }

    @Test
    @DisplayName("가게 정보 수정 실패 테스트 - 존재하지 않는 가게")
    void updateStore_NotFoundStore() {
        // given
        Long storeId = 999L;
        Long regionId = 2L;
        StoreRequestDto request = new StoreRequestDto("Updated Store", Category.CHICKEN, regionId);

        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> updateStoreService.updateStore(storeId, request));
    }

    @Test
    @DisplayName("가게 정보 수정 실패 테스트 - 존재하지 않는 지역")
    void updateStore_NotFoundRegion() {
        // given
        Long storeId = 1L;
        Long regionId = 999L;
        StoreRequestDto request = new StoreRequestDto("Updated Store", Category.CHICKEN, regionId);
        Store existingStore = new Store(storeId, "Old Store", Category.PIZZA, new Region(2L, "Region Code", "Region Name"));

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(existingStore));
        when(regionRepository.findById(regionId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> updateStoreService.updateStore(storeId, request));
    }
}