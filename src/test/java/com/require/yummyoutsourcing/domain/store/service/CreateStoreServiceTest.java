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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateStoreServiceTest {

    @InjectMocks
    private CreateStoreService createStoreService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private StoreMapper storeMapper;

    @Test
    @DisplayName("가게 등록 성공 테스트")
    void shouldRegisterStoreSuccessfully() {
        // given
        Region mockRegion = createMockRegion();
        StoreRequestDto requestDto = createMockRequestDto(mockRegion.getId());
        Store mockStore = createMockStore(mockRegion);
        StoreResponseDto responseDto = createMockResponseDto(mockRegion);

        // when
        mockRepositoryAndMapper(requestDto, mockRegion, mockStore, responseDto);

        // then
        ApiResponse<StoreResponseDto> response = createStoreService.createStore(requestDto);

        verifySuccessfulResponse(response);
    }

    @Test
    @DisplayName("존재하지 않는 지역 ID로 가게 등록 실패 테스트")
    void shouldFailToRegisterStoreWithInvalidRegionId() {
        // given
        StoreRequestDto requestDto = createMockRequestDto(999L);

        // when
        when(regionRepository.findById(999L)).thenThrow(new IllegalArgumentException("가게 등록 실패"));

        // then
        assertThrows(IllegalArgumentException.class, () -> createStoreService.createStore(requestDto));
    }

    @Test
    @DisplayName("빈 가게 이름으로 등록 실패 테스트")
    void shouldFailToRegisterStoreWithEmptyName() {
        // given
        StoreRequestDto requestDto = StoreRequestDto.builder()
                .name("")
                .category(Category.CHICKEN)
                .regionId(1L)
                .build();

        // then
        assertThrows(IllegalArgumentException.class, () -> createStoreService.createStore(requestDto));
    }

    // Mock Region 객체 생성
    private Region createMockRegion() {
        return Region.builder()
                .id(1L)
                .regionCode("12345")
                .regionName("Sample Region")
                .build();
    }

    // Mock StoreRequestDto 객체 생성
    private StoreRequestDto createMockRequestDto(Long regionId) {
        return StoreRequestDto.builder()
                .name("맛집가게")
                .category(Category.CHICKEN)
                .regionId(regionId)
                .build();
    }

    // Mock Store 객체 생성
    private Store createMockStore(Region region) {
        return Store.builder()
                .id(1L)
                .name("맛집가게")
                .category(Category.CHICKEN)
                .region(region)
                .build();
    }

    // Mock StoreResponseDto 객체 생성
    private StoreResponseDto createMockResponseDto(Region region) {
        return StoreResponseDto.builder()
                .id(1L)
                .name("맛집가게")
                .category(Category.CHICKEN)
                .regionId(region.getId())
                .build();
    }

    // Mock 동작 정의
    private void mockRepositoryAndMapper(StoreRequestDto requestDto, Region mockRegion, Store mockStore, StoreResponseDto responseDto) {
        when(regionRepository.findById(mockRegion.getId())).thenReturn(Optional.of(mockRegion));
        when(storeMapper.toEntity(requestDto, mockRegion)).thenReturn(mockStore);
        when(storeRepository.save(mockStore)).thenReturn(mockStore);
        when(storeMapper.toDto(mockStore)).thenReturn(responseDto);
    }

    // 성공적인 응답 검증
    private void verifySuccessfulResponse(ApiResponse<StoreResponseDto> response) {
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("가게 등록 성공");
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getName()).isEqualTo("맛집가게");
        assertThat(response.getData().getCategory()).isEqualTo(Category.CHICKEN);
    }
}