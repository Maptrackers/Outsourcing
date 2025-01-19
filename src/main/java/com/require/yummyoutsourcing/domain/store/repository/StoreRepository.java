package com.require.yummyoutsourcing.domain.store.repository;

import com.require.yummyoutsourcing.domain.region.model.Region;
import com.require.yummyoutsourcing.domain.store.model.Category;
import com.require.yummyoutsourcing.domain.store.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long> {

    /**
     * 필터 조건을 기반으로 Store 엔티티를 조회합니다.
     *
     * @param category   검색할 카테고리 (null 허용)
     * @param regionId   검색할 지역 ID (null 허용)
     * @param name       검색할 가게 이름 (null 허용)
     * @param pageable   페이지 정보
     * @return 검색 조건에 맞는 Store 목록
     */
    @Query("""
       SELECT s FROM Store s 
       WHERE (:category IS NULL OR s.category = :category) 
         AND (:regionId IS NULL OR s.region.id = :regionId) 
         AND (:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')))
       """)
    Page<Store> findByFilters(@Param("category") Category category,
                              @Param("regionId") Long regionId,
                              @Param("name") String name,
                              Pageable pageable);

    Long region(Region region);
}