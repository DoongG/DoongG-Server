package com.merge.doongG.service;

import com.merge.doongG.domain.User;
import com.merge.doongG.dto.GetCartDTO;

import java.util.List;
import java.util.UUID;

public interface CartService {

    // 장바구니 생성
    void createCart(User user);

    // 장바구니 조회
    List<GetCartDTO> getCart(UUID uuid);

    String addCart(UUID uuid, Long productID, int quantity);
}