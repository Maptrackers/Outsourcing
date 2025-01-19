package com.require.yummyoutsourcing.domain.store.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.require.yummyoutsourcing.common.ApiResponse;
import com.require.yummyoutsourcing.domain.store.dto.StoreRequestDto;
import com.require.yummyoutsourcing.domain.store.dto.StoreResponseDto;
import com.require.yummyoutsourcing.domain.store.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/store - 가게 등록 성공")
    void createStore_Success() throws Exception {
        // given
        StoreRequestDto request = createTestStoreRequest("맛집가게", Category.CHICKEN, 1L);

        // when
        StoreResponseDto response = createStore(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("GET /api/v1/store/{storeId} - ID로 가게 조회 성공")
    void getStoreById_Success() throws Exception {
        // given
        StoreRequestDto request = createTestStoreRequest("맛집가게", Category.CHICKEN, 1L);
        StoreResponseDto createdStore = createStore(request);

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/store/{storeId}", createdStore.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        ApiResponse<StoreResponseDto> apiResponse = objectMapper.readValue(response.getContentAsString(),
                objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, StoreResponseDto.class));
        StoreResponseDto storeResponseDto = apiResponse.getData();

        assertThat(storeResponseDto).isNotNull();
        assertThat(storeResponseDto.getId()).isEqualTo(createdStore.getId());
    }

    @Test
    @DisplayName("GET /api/v1/store - 전체 가게 목록 조회 성공")
    void getAllStores_Success() throws Exception {
        // given
        createStore(createTestStoreRequest("맛집가게1", Category.CHICKEN, 1L));
        createStore(createTestStoreRequest("맛집가게2", Category.JOKBAL_BOSSAM, 2L));

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/store")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        JsonNode responseJson = objectMapper.readTree(response.getContentAsString());
        assertThat(responseJson.get("data").get("stores")).isNotNull();
    }

    @Test
    @DisplayName("DELETE /api/v1/store/{storeId} - 가게 삭제 성공")
    void deleteStore_Success() throws Exception {
        // given
        StoreResponseDto createdStore = createStore(createTestStoreRequest("삭제될 가게", Category.CHICKEN, 1L));

        // when
        MockHttpServletResponse response = mockMvc.perform(delete("/api/v1/store/{storeId}", createdStore.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("PATCH /api/v1/store/{storeId} - 가게 정보 수정 성공")
    void updateStore_Success() throws Exception {
        // given
        StoreResponseDto createdStore = createStore(createTestStoreRequest("수정 전 가게", Category.CHICKEN, 1L));
        StoreRequestDto updateRequest = createTestStoreRequest("수정된 가게", Category.CHINESE, 2L);

        // when
        MockHttpServletResponse response = mockMvc.perform(patch("/api/v1/store/{storeId}", createdStore.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private StoreRequestDto createTestStoreRequest(String name, Category category, Long regionId) {
        return StoreRequestDto.builder()
                .name(name)
                .category(category)
                .regionId(regionId)
                .build();
    }

    private StoreResponseDto createStore(StoreRequestDto request) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        ApiResponse<StoreResponseDto> apiResponse = objectMapper.readValue(response.getContentAsString(),
                objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, StoreResponseDto.class));
        return apiResponse.getData();
    }
}