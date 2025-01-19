package com.require.yummyoutsourcing.domain.region.repository;

import com.require.yummyoutsourcing.domain.region.model.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {

    /**
     * 특정 지역 코드로 시작하는 Region 데이터를 페이지 단위로 조회합니다.
     *
     * @param regionCode 조회할 지역 코드의 접두사
     * @param pageable   페이지네이션 정보를 포함한 Pageable 객체
     * @return 특정 지역 코드로 시작하는 Region 데이터의 페이지 객체
     */
    Page<Region> findByRegionCodeStartingWith(String regionCode, Pageable pageable);
}
