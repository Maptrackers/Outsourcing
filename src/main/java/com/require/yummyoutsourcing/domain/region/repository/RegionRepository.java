package com.require.yummyoutsourcing.domain.region.repository;

import com.require.yummyoutsourcing.domain.region.model.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Page<Region> findByRegionCodeStartingWith(String regionCode, Pageable pageable);
}
