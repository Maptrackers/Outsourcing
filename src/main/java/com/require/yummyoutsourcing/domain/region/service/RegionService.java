package com.require.yummyoutsourcing.domain.region.service;

import com.require.yummyoutsourcing.common.ApiResponse;
import com.require.yummyoutsourcing.common.PaginationUtils;
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
     * 지정된 지역 코드로 시작하는 지역 목록을 페이지 단위로 조회합니다.
     *
     * @param regionCode 조회할 지역 코드의 접두사
     * @param page       조회할 페이지 번호 (1부터 시작)
     * @param size       페이지당 항목 수
     * @return 지역 목록과 페이지네이션 정보를 포함한 ApiResponse 객체
     */
    public ApiResponse<Object> getRegions(String regionCode, int page, int size) {
        PageRequest pageable = createPageRequest(page, size);
        Page<Region> regionsPage = fetchRegions(regionCode, pageable);

        validateRegionExist(regionsPage);

        List<RegionResponseDto> regionDtoList = convertToDtoList(regionsPage.getContent());
        Map<String, Object> pageableInfo = createPageableInfo(page, size, regionsPage);

        return buildApiResponse(regionDtoList, pageableInfo);
    }

    // 페이지 요청 객체를 생성합니다.
    private PageRequest createPageRequest(int page, int size) {
        return PaginationUtils.createPageRequest(page, size);
    }

    // 데이터베이스에서 지역 정보를 조회합니다.
    private Page<Region> fetchRegions(String regionCode, PageRequest pageable) {
        return regionRepository.findByRegionCodeStartingWith(regionCode, pageable);
    }

    // 지역 목록이 비어 있는 경우 예외를 던집니다.
    private static void validateRegionExist(Page<Region> regionsPage) {
        if (regionsPage.isEmpty())
            throw new IllegalArgumentException("조회된 지역 목록이 없습니다");
    }

    // 조회된 지역 엔티티 리스트를 DTO 리스트로 변환합니다.
    private List<RegionResponseDto> convertToDtoList(List<Region> regions) {
        return regionMapper.toDtoList(regions);
    }

    // 페이지네이션 정보를 생성합니다.
    private Map<String, Object> createPageableInfo(int page, int size, Page<Region> regionsPage) {
        return Map.of(
                "page", page,
                "size", size,
                "totalPages", regionsPage.getTotalPages(),
                "totalItems", regionsPage.getTotalElements()
        );
    }

    // ApiResponse 객체를 생성합니다.
    private ApiResponse<Object> buildApiResponse(
            List<RegionResponseDto> regionDtoList, Map<String, Object> pageableInfo) {
        return ApiResponse.builder()
                .data(Map.of(
                        "regions", regionDtoList,
                        "pageable", pageableInfo
                ))
                .message("지역 목록이 성공적으로 조회되었습니다.")
                .build();
    }
}