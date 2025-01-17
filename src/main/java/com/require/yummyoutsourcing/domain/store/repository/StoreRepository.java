package com.require.yummyoutsourcing.domain.store.repository;

import com.require.yummyoutsourcing.domain.store.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long> {
    @Query("SELECT s FROM Store s WHERE " +
            "(:category IS NULL OR s.category = :category) AND " +
            "(:regionCode IS NULL OR s.region.regionCode = :regionCode) AND " +
            "(:name IS NULL OR s.name LIKE %:name%)")
    Page<Store> findByFilters(@Param("category") String category,
                              @Param("regionCode") String regionCode,
                              @Param("name") String name,
                              Pageable pageable);
}
