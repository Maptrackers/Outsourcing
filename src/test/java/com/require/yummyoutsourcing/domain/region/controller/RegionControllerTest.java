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
    void getRegions_shouldReturnRegionListSuccessfully() throws Exception {
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

        Map<String, Object> data = extractData(jsonResponse);
        List<Map<String, Object>> regions = extractRegions(data);
        Map<String, Object> pageable = extractPageable(data);

        // then
        validateRegions(regions, getExpectedRegions());
        validatePageable(pageable, 1, 15);
    }

    // JSON 응답에서 "data" 추출
    private Map<String, Object> extractData(Map<String, Object> jsonResponse) {
        return (Map<String, Object>) jsonResponse.get("data");
    }

    // "data"에서 "regions" 추출
    private List<Map<String, Object>> extractRegions(Map<String, Object> data) {
        return (List<Map<String, Object>>) data.get("regions");
    }

    // "data"에서 "pageable" 추출
    private Map<String, Object> extractPageable(Map<String, Object> data) {
        return (Map<String, Object>) data.get("pageable");
    }

    // 예상 지역 데이터를 반환
    private List<Map<String, String>> getExpectedRegions() {
        return List.of(
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
    }

    // "regions"의 데이터를 검증
    private void validateRegions(List<Map<String, Object>> regions, List<Map<String, String>> expectedRegions) {
        for (int i = 0; i < expectedRegions.size(); i++) {
            assertRegionEquals(expectedRegions.get(i), regions.get(i));
        }
    }

    // 개별 지역 정보 검증
    private void assertRegionEquals(Map<String, String> expected, Map<String, Object> actual) {
        assertThat(actual.get("regionCode")).isEqualTo(expected.get("regionCode"));
        assertThat(actual.get("regionName")).isEqualTo(expected.get("regionName"));
    }

    // "pageable" 데이터 검증
    private void validatePageable(Map<String, Object> pageable, int expectedPage, int expectedSize) {
        assertThat(pageable.get("page")).isEqualTo(expectedPage);
        assertThat(pageable.get("size")).isEqualTo(expectedSize);
    }
}