package com.require.yummyoutsourcing.domain.store.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.require.yummyoutsourcing.domain.store.dto.StoreRequestDto;
import com.require.yummyoutsourcing.domain.store.dto.StoreResponseDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
                .category("양식")
                .regionId(1168010100)
                .build();

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        StoreResponseDto storeResponseDto = objectMapper.readValue(response.getContentAsString(), StoreResponseDto.class);
        assertThat(storeResponseDto.getId()).isGreaterThan(0);
    }
}
