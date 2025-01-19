package com.require.yummyoutsourcing.domain.store.service;

import com.require.yummyoutsourcing.common.ApiResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadStoreServiceTest {

    @InjectMocks
    private ReadStoreService readStoreService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreMapper storeMapper;

    @Test
    @DisplayName("가게 조회 성공 테스트")
    void getStoreById_Success() {
        // given
        Long storeId = 1L;
        Store store = new Store();
        StoreResponseDto storeResponseDto = new StoreResponseDto();

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(storeMapper.toDto(store)).thenReturn(storeResponseDto);

        // when
        ApiResponse<StoreResponseDto> response = readStoreService.getStoreById(storeId);

        // then
        assertThat(response.getMessage()).isEqualTo("가게 조회 성공");
        verify(storeRepository, times(1)).findById(storeId);
        verify(storeMapper, times(1)).toDto(store);
    }

    @Test
    @DisplayName("가게 조회 실패 테스트 - 존재하지 않는 ID")
    void getStoreById_NotFound() {
        // given
        Long storeId = 1L;
        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> readStoreService.getStoreById(storeId));
        verify(storeRepository, times(1)).findById(storeId);
    }

    @Test
    @DisplayName("가게 목록 필터 조회 성공 테스트")
    void getFilteredStoreList_Success() {
        // given
        Category category = Category.CHICKEN;
        Long regionId = 1L;
        String name = "Test Store";
        int page = 1;
        int size = 10;

        Store store1 = new Store();
        Store store2 = new Store();
        StoreResponseDto dto1 = new StoreResponseDto();
        StoreResponseDto dto2 = new StoreResponseDto();

        Page<Store> storePage = new PageImpl<>(List.of(store1, store2), PageRequest.of(page - 1, size), 2);

        when(storeRepository.findByFilters(category, regionId, name, PageRequest.of(page - 1, size))).thenReturn(storePage);
        when(storeMapper.toDtoList(storePage.getContent())).thenReturn(List.of(dto1, dto2));

        // when
        ApiResponse<Object> response = readStoreService.getFilteredStoreList(category, regionId, name, page, size);

        // then
        assertThat(response.getMessage()).isEqualTo("가게 목록 조회 성공");
        assertThat(response.getData()).isNotNull();
        verify(storeRepository, times(1)).findByFilters(category, regionId, name, PageRequest.of(page - 1, size));
        verify(storeMapper, times(1)).toDtoList(storePage.getContent());
    }

    @Test
    @DisplayName("가게 목록 조회 실패 테스트 - 필터 조건 불일치")
    void getFilteredStoreList_NoResults() {
        // given
        Category category = Category.CHICKEN;
        Long regionId = 1L;
        String name = "Test Store";
        int page = 1;
        int size = 10;

        when(storeRepository.findByFilters(category, regionId, name, PageRequest.of(page - 1, size)))
                .thenReturn(Page.empty(PageRequest.of(page - 1, size)));

        // when & then
        assertThrows(IllegalArgumentException.class, () -> readStoreService.getFilteredStoreList(category, regionId, name, page, size));
        verify(storeRepository, times(1)).findByFilters(category, regionId, name, PageRequest.of(page - 1, size));
    }

    @Test
    @DisplayName("페이지 정보 유효성 테스트")
    void validatePageInfo() {
        // given
        int page = -1;
        int size = 0;

        // when & then
        assertThrows(IllegalArgumentException.class, () -> readStoreService.getFilteredStoreList(null, null, null, page, size));
    }
}