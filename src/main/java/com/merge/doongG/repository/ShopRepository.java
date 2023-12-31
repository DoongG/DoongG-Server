package com.merge.doongG.repository;

import com.merge.doongG.domain.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Product, Long> {
    // 최근 상품 10개 배열
    List<Product> findTop10ByOrderByCreatedAtDesc();

    // 조회수 높은 순으로 상품 10개 배열
    List<Product> findTop10ByOrderByViewCountDesc();

    // 상품 하나 조회
    Optional<Product> findByProductID(Long productID);

    // 카테고리 별 상품 조회
    List<Product> findAllByCategory(String category);

    // 조회수 증가 메소드
    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.viewCount = p.viewCount + 1 WHERE p.productID = :productID")
    void updateViewCount(@Param("productID") Long productID);
}
