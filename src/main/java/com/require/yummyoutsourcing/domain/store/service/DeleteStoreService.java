package com.require.yummyoutsourcing.domain.store.service;

import com.require.yummyoutsourcing.common.ApiResponse;
import com.require.yummyoutsourcing.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteStoreService {

    private final StoreRepository storeRepository;

    /**
     * 가게를 삭제합니다.
     *
     * @param storeId 삭제할 가게의 ID
     * @return 삭제 성공 메시지가 포함된 ApiResponse
     */
    public ApiResponse<Object> deleteStore(Long storeId) {
        validateStoreExistence(storeId);
        performDelete(storeId);
        return buildSuccessResponse();
    }

    // 주어진 ID의 가게 존재 여부를 확인
    private void validateStoreExistence(Long storeId) {
        if (!storeRepository.existsById(storeId))
            throw new IllegalArgumentException("가게 삭제 실패");
    }

    // 가게 삭제 수행
    private void performDelete(Long storeId) {
        storeRepository.deleteById(storeId);
    }

    // 성공 응답을 생성
    private ApiResponse<Object> buildSuccessResponse() {
        return ApiResponse.builder()
                .data(null)
                .message("가게 삭제 성공")
                .build();
    }
}