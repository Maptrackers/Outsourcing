package com.require.yummyoutsourcing.domain.store.service;

import com.require.yummyoutsourcing.common.ApiResponse;
import com.require.yummyoutsourcing.domain.store.dto.StoreResponseDto;
import com.require.yummyoutsourcing.domain.store.mapper.StoreMapper;
import com.require.yummyoutsourcing.domain.store.model.Store;
import com.require.yummyoutsourcing.domain.store.repository.StoreRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadStoreServiceTest {

    @InjectMocks
    private ReadStoreService readStoreService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreMapper storeMapper;

    @Test
    void test1() {
        Long storeId = 1L;
        Store store = new Store();
        StoreResponseDto storeResponseDto = new StoreResponseDto();

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(storeMapper.toDto(store)).thenReturn(storeResponseDto);

        ApiResponse<StoreResponseDto> response = readStoreService.readStore(storeId);

        assertThat(response.getMessage()).isEqualTo("가게 조회 성공");
    }

    @Test
    void test2() {
        Long storeId = 1L;
        when(storeRepository.findById(storeId)).thenThrow(new IllegalArgumentException("가게 조회 실패"));

        assertThrows(IllegalArgumentException.class, () -> readStoreService.readStore((storeId)));
    }

    @Test
    void test3() {
        String category = "CHICKEN";
        String regionCode = "123";
        String name = "Test Store";
        int page = 1;
        int size = 10;

        Store store1 = new Store();
        Store store2 = new Store();
        StoreResponseDto dto1 = new StoreResponseDto();
        StoreResponseDto dto2 = new StoreResponseDto();

        Page<Store> storePage = new PageImpl<>(List.of(store1, store2), PageRequest.of(page - 1, size), 2);

        when(storeRepository.findByFilters(category, regionCode, name, PageRequest.of(page - 1, size))).thenReturn(storePage);
        when(storeMapper.toDtoList(storePage.getContent())).thenReturn(List.of(dto1, dto2));

        ApiResponse<Object> response = readStoreService.readAllStores(category, regionCode, name, page, size);

        assertThat(response.getMessage()).isEqualTo("가게 목록 조회 성공");
    }

    @Test
    void test4() {
        String category = "CHICKEN";
        String regionCode = "123";
        String name = "Test Store";
        int page = 1;
        int size = 10;

        Page<Store> storePage = new PageImpl<>(List.of(), PageRequest.of(page - 1, size), 2);

        when(storeRepository.findByFilters(category, regionCode, name, PageRequest.of(page - 1, size))).thenThrow(new IllegalArgumentException("가게 목록이 존재하지 않습니다"));

        assertThrows(IllegalArgumentException.class, () -> readStoreService.readAllStores(category, regionCode, name, page, size));
    }
}