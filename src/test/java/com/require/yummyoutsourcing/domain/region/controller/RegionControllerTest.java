package com.require.yummyoutsourcing.domain.region.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RegionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/v1/regions - 지역 목록 조회 성공")
    void shouldReturnRegionListSuccessfully() throws Exception {
        // given
        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/regions")
                        .param("regionCode", "11680")
                        .param("page", "1")
                        .param("size", "15")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // when
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Map<String, Object> jsonResponse = objectMapper.readValue(response.getContentAsString(), Map.class);

        Map<String, Object> data = (Map<String, Object>) jsonResponse.get("data");
        List<Map<String, Object>> regions = (List<Map<String, Object>>) data.get("regions");
        Map<String, Object> pageable = (Map<String, Object>) data.get("pageable");

        // then
        validateRegions(regions);
        validatePageable(pageable);
    }

    private void validateRegions(List<Map<String, Object>> regions) {
        List<Map<String, String>> expectedRegions = List.of(
                Map.of("regionCode", "1168000000", "regionName", "서울특별시 강남구"),
                Map.of("regionCode", "1168010100", "regionName", "서울특별시 강남구 역삼동"),
                Map.of("regionCode", "1168010300", "regionName", "서울특별시 강남구 개포동"),
                Map.of("regionCode", "1168010400", "regionName", "서울특별시 강남구 청담동"),
                Map.of("regionCode", "1168010500", "regionName", "서울특별시 강남구 삼성동"),
                Map.of("regionCode", "1168010600", "regionName", "서울특별시 강남구 대치동"),
                Map.of("regionCode", "1168010700", "regionName", "서울특별시 강남구 신사동"),
                Map.of("regionCode", "1168010800", "regionName", "서울특별시 강남구 논현동"),
                Map.of("regionCode", "1168011000", "regionName", "서울특별시 강남구 압구정동"),
                Map.of("regionCode", "1168011100", "regionName", "서울특별시 강남구 세곡동"),
                Map.of("regionCode", "1168011200", "regionName", "서울특별시 강남구 자곡동"),
                Map.of("regionCode", "1168011300", "regionName", "서울특별시 강남구 율현동"),
                Map.of("regionCode", "1168011400", "regionName", "서울특별시 강남구 일원동"),
                Map.of("regionCode", "1168011500", "regionName", "서울특별시 강남구 수서동"),
                Map.of("regionCode", "1168011800", "regionName", "서울특별시 강남구 도곡동")
        );

        for (int i = 0; i < expectedRegions.size(); i++) {
            Map<String, String> expectedRegion = expectedRegions.get(i);
            Map<String, Object> actualRegion = regions.get(i);

            assertThat(actualRegion.get("regionCode")).isEqualTo(expectedRegion.get("regionCode"));
            assertThat(actualRegion.get("regionName")).isEqualTo(expectedRegion.get("regionName"));
        }
    }

    private void validatePageable(Map<String, Object> pageable) {
        assertThat(pageable.get("page")).isEqualTo(1);
        assertThat(pageable.get("size")).isEqualTo(15);
    }
}
