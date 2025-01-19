package com.require.yummyoutsourcing.domain.store.service;

import com.require.yummyoutsourcing.common.ApiResponse;
import com.require.yummyoutsourcing.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteStoreServiceTest {

    @InjectMocks
    private DeleteStoreService deleteStoreService;

    @Mock
    private StoreRepository storeRepository;

    @Test
    @DisplayName("가게 삭제 성공 테스트")
    void deleteStore_Success() {
        // given
        Long storeId = 1L;
        when(storeRepository.existsById(storeId)).thenReturn(true);

        // when
        ApiResponse<Object> response = deleteStoreService.deleteStore(storeId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("가게 삭제 성공");

        // verify
        verify(storeRepository, times(1)).existsById(storeId);
        verify(storeRepository, times(1)).deleteById(storeId);
    }

    @Test
    @DisplayName("가게 삭제 실패 테스트 - 존재하지 않는 ID")
    void deleteStore_NotFound() {
        // given
        Long nonExistentId = 999L;
        when(storeRepository.existsById(nonExistentId)).thenReturn(false);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> deleteStoreService.deleteStore(nonExistentId));

        // verify
        verify(storeRepository, times(1)).existsById(nonExistentId);
        verify(storeRepository, never()).deleteById(nonExistentId);
    }

    @Test
    @DisplayName("가게 삭제 실패 테스트 - 삭제 중 예외 발생")
    void deleteStore_ExceptionDuringDeletion() {
        // given
        Long storeId = 2L;
        when(storeRepository.existsById(storeId)).thenReturn(true);
        doThrow(new RuntimeException("삭제 중 에러 발생")).when(storeRepository).deleteById(storeId);

        // when & then
        assertThrows(RuntimeException.class, () -> deleteStoreService.deleteStore(storeId));

        // verify
        verify(storeRepository, times(1)).existsById(storeId);
        verify(storeRepository, times(1)).deleteById(storeId);
    }
}