package com.require.yummyoutsourcing.domain.store.controller;

import com.require.yummyoutsourcing.common.ApiResponse;
import com.require.yummyoutsourcing.domain.store.dto.StoreRequestDto;
import com.require.yummyoutsourcing.domain.store.dto.StoreResponseDto;
import com.require.yummyoutsourcing.domain.store.service.CreateStoreService;
import com.require.yummyoutsourcing.domain.store.service.ReadStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store")
@RequiredArgsConstructor
public class StoreController {

    private final CreateStoreService createStoreService;
    private final ReadStoreService readStoreService;

    @PostMapping
    public ResponseEntity<ApiResponse<StoreResponseDto>> createStore(@RequestBody StoreRequestDto request) {
        ApiResponse<StoreResponseDto> response = createStoreService.createStore(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResponse<StoreResponseDto>> getStore(@PathVariable(name = "storeId") Long storeId) {
        ApiResponse<StoreResponseDto> response = readStoreService.readStore(storeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getStores(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "regionCode", required = false) String regionCode,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        ApiResponse<Object> response = readStoreService.readAllStores(category, regionCode, name, page, size);
        return ResponseEntity.ok(response);
    }
}
