package com.require.yummyoutsourcing.domain.store.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.require.yummyoutsourcing.common.ApiResponse;
import com.require.yummyoutsourcing.domain.store.dto.StoreRequestDto;
import com.require.yummyoutsourcing.domain.store.dto.StoreResponseDto;
import com.require.yummyoutsourcing.domain.store.model.Category;
import jakarta.servlet.http.HttpServletResponse;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StoreControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void test1() throws Exception {
        // given
        StoreRequestDto request = StoreRequestDto.builder()
                .name("맛집가게")
                .category(Category.CHICKEN)
                .regionId(1L)
                .build();

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        ApiResponse<StoreResponseDto> apiResponse = objectMapper.readValue(response.getContentAsString(),
                objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, StoreResponseDto.class));

        StoreResponseDto storeResponseDto = apiResponse.getData();
        assertThat(storeResponseDto).isNotNull();
        assertThat(storeResponseDto.getId()).isGreaterThan(0);
    }

    @Test
    void test2() throws Exception {
        StoreRequestDto request = StoreRequestDto.builder()
                .name("맛집가게")
                .category(Category.CHICKEN)
                .regionId(1L)
                .build();

        MockHttpServletResponse createResponse = mockMvc.perform(post("/api/v1/store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse();

        String responseBody = createResponse.getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        StoreResponseDto createStore = objectMapper.treeToValue(jsonNode.get("data"), StoreResponseDto.class);
        Long storeId = createStore.getId();

        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/store/{storeId}", storeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void test3() throws Exception {
        StoreRequestDto request1 = StoreRequestDto.builder()
                .name("맛집가게")
                .category(Category.CHICKEN)
                .regionId(1L)
                .build();

        StoreRequestDto request2 = StoreRequestDto.builder()
                .name("맛집가게")
                .category(Category.JOKBAL_BOSSAM)
                .regionId(2L)
                .build();

        mockMvc.perform(post("/api/v1/store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andReturn().getResponse();

        mockMvc.perform(post("/api/v1/store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andReturn().getResponse();

        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/store")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void deleteStoreTest() throws Exception {
        Long storeId = 9L;

        MockHttpServletResponse response = mockMvc.perform(delete("/api/v1/store/{storeId}", storeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void updateStoreTest() throws Exception {
        StoreRequestDto request = StoreRequestDto.builder()
                .name("수정된 맛집가게")
                .category(Category.CHINESE)
                .regionId(2L)
                .build();

        MockHttpServletResponse response = mockMvc.perform(patch("/api/v1/store/{storeId}", 9L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
