package com.require.yummyoutsourcing.domain.region.service;

import com.require.yummyoutsourcing.common.ApiResponse;
import com.require.yummyoutsourcing.common.PaginationUtils;
import com.require.yummyoutsourcing.domain.region.dto.RegionResponseDto;
import com.require.yummyoutsourcing.domain.region.mapper.RegionMapper;
import com.require.yummyoutsourcing.domain.region.model.Region;
import com.require.yummyoutsourcing.domain.region.repository.RegionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegionServiceTest {

    @InjectMocks
    private RegionService regionService;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private RegionMapper regionMapper;

    private String regionCode;
    private int page;
    private int size;
    private PageRequest pageable;
    private List<Region> regions;
    private Page<Region> regionsPage;
    private List<RegionResponseDto> regionDtoList;

    @BeforeEach
    void setUp() {
        initializeTestParameters();
        initializeMockRegions();
        initializeRegionDtoList();
    }

    @Test
    @DisplayName("지역 목록을 정상적으로 조회하면 ApiResponse를 반환해야 한다")
    void testGetRegions_ShouldReturnApiResponseWithPagedRegions() {
        // given
        when(regionRepository.findByRegionCodeStartingWith(regionCode, pageable)).thenReturn(regionsPage);
        when(regionMapper.toDtoList(regions)).thenReturn(regionDtoList);

        // when
        ApiResponse<Object> response = regionService.getRegions(regionCode, page, size);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("지역 목록이 성공적으로 조회되었습니다.");

        Map<String, Object> responseData = (Map<String, Object>) response.getData();

        assertThat(responseData.get("regions")).isEqualTo(regionDtoList);

        Map<String, Object> pageableInfo = (Map<String, Object>) responseData.get("pageable");
        assertThat(pageableInfo.get("page")).isEqualTo(page);
        assertThat(pageableInfo.get("size")).isEqualTo(size);
        assertThat(pageableInfo.get("totalPages")).isEqualTo(1);
        assertThat(pageableInfo.get("totalItems")).isEqualTo((long) regions.size());
    }

    private void initializeTestParameters() {
        regionCode = "11680";
        page = 1;
        size = 10;
        pageable = PaginationUtils.createPageRequest(page, size);
    }

    private void initializeMockRegions() {
        regions = List.of(
                Region.builder()
                        .id(1L)
                        .regionCode("1168000000")
                        .regionName("서울특별시 강남구")
                        .build(),
                Region.builder()
                        .id(2L)
                        .regionCode("1168010100")
                        .regionName("서울특별시 강남구 역삼동")
                        .build()
        );

        regionsPage = new PageImpl<>(regions, pageable, regions.size());
    }

    private void initializeRegionDtoList() {
        regionDtoList = List.of(
                RegionResponseDto.builder()
                        .regionId(1L)
                        .regionCode("1168000000")
                        .regionName("서울특별시 강남구")
                        .build(),
                RegionResponseDto.builder()
                        .regionId(2L)
                        .regionCode("1168010100")
                        .regionName("서울특별시 강남구 역삼동")
                        .build()
        );
    }
}