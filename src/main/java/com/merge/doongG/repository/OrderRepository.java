package com.merge.doongG.repository;

import com.merge.doongG.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // 유저로 주문내역 조회
    List<Order> findByUserId(Long userId);
}
