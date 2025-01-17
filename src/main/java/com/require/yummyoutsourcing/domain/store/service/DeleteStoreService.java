package com.require.yummyoutsourcing.domain.store.service;

import com.require.yummyoutsourcing.common.ApiResponse;
import com.require.yummyoutsourcing.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteStoreService {

    private final StoreRepository storeRepository;

    public ApiResponse<Object> deleteStore(Long storeId) {
        if (!storeRepository.existsById(storeId))
            throw new IllegalArgumentException("가게 삭제 실패");
        storeRepository.deleteById(storeId);
        return ApiResponse.builder()
                .data(null)
                .message("가게 삭제 성공")
                .build();
    }
}
