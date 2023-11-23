package com.merge.doongG.repository;

import com.merge.doongG.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopRepository extends JpaRepository<Product, Long> {
    List<Product> findTop10ByOrderByCreatedAtDesc(); // 최근 상품 10개 배열

    List<Product> findTop10ByOrderByViewCountDesc(); // 조회수 높은 순으로 상품 10개 배열
}
