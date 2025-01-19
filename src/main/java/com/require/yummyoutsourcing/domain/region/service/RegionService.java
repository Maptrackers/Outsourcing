package com.require.yummyoutsourcing.domain.region.service;

import com.require.yummyoutsourcing.common.ApiResponse;
import com.require.yummyoutsourcing.domain.region.dto.RegionResponseDto;
import com.require.yummyoutsourcing.domain.region.mapper.RegionMapper;
import com.require.yummyoutsourcing.domain.region.model.Region;
import com.require.yummyoutsourcing.domain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;
    private final RegionMapper regionMapper;

    /**
     * 지역 코드 접두사로 필터링하여 지역 목록을 조회합니다.
     *
     * @param regionCode 지역 코드 접두사
     * @param page       페이지 번호 (1부터 시작)
     * @param size       페이지 크기
     * @return 지역 목록 및 페이지 정보가 포함된 ApiResponse
     */
    public ApiResponse<Object> getRegions(String regionCode, int page, int size) {
        PageRequest pageable = buildPageRequest(page, size);
        Page<Region> regionsPage = fetchRegions(regionCode, pageable);

        List<RegionResponseDto> regionDtoList = convertToDtoList(regionsPage.getContent());
        Map<String, Object> pageableInfo = createPageableInfo(page, size, regionsPage);

        return buildApiResponse(regionDtoList, pageableInfo);
    }

    // 페이지 요청 정보를 생성합니다.
    private PageRequest buildPageRequest(int page, int size) {
        return PageRequest.of(page - 1, size);
    }

    // 데이터베이스에서 지역 목록을 조회합니다.
    private Page<Region> fetchRegions(String regionCode, PageRequest pageable) {
        Page<Region> regionsPage = regionRepository.findByRegionCodeStartingWith(regionCode, pageable);
        if (regionsPage.isEmpty())
            throw new IllegalArgumentException("지역 리스트 조회 실패");

        return regionsPage;
    }

    // 조회된 지역 엔티티를 DTO 리스트로 변환합니다.
    private List<RegionResponseDto> convertToDtoList(List<Region> regions) {
        return regionMapper.toDtoList(regions);
    }

    // ApiResponse 객체를 생성합니다.
    private ApiResponse<Object> buildApiResponse(
            List<RegionResponseDto> regionDtoList, Map<String, Object> pageableInfo) {
        return ApiResponse.builder()
                .data(Map.of(
                        "regions", regionDtoList,
                        "pageable", pageableInfo
                ))
                .message("지역 리스트 조회 성공")
                .build();
    }

    // 페이지네이션 정보를 생성합니다.
    private static Map<String, Object> createPageableInfo(int page, int size, Page<?> pageData) {
        return Map.of(
                "page", page,
                "size", size,
                "totalPages", pageData.getTotalPages(),
                "totalItems", pageData.getTotalElements()
        );
    }
}