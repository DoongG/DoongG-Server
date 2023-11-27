package com.merge.doongG.repository;

import com.merge.doongG.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserUuid(UUID uuid);
}
