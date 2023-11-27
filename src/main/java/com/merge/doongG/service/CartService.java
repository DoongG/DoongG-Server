package com.merge.doongG.service;

import com.merge.doongG.domain.Cart;
import com.merge.doongG.domain.User;
import com.merge.doongG.dto.GetCartDTO;
import com.merge.doongG.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    // 장바구니 생성
    public void createCart(User user) {
        Cart cart = Cart.builder()
                .user(user)
                .build();

        cartRepository.save(cart);
    }

    // 장바구니 조회
    public List<GetCartDTO> getCart(UUID uuid) {
        // uuid로 장바구니 찾기
        Cart cart = cartRepository.findByUserUuid(uuid);
        List<GetCartDTO> list = new ArrayList<>();

        // cartdetails에서 상품 정보 가져오기
        for (int i = 0; i < cart.getCartDetails().size(); i++) {
            GetCartDTO getCartDTO = GetCartDTO.builder()
                    .productID(cart.getCartDetails().get(i).getProduct().getProductID())
                    .productName(cart.getCartDetails().get(i).getProduct().getProductName())
                    .productImage(cart.getCartDetails().get(i).getProduct().getProductImage())
                    .price(cart.getCartDetails().get(i).getProduct().getPrice())
                    .discountedPrice(cart.getCartDetails().get(i).getProduct().getDiscountedPrice())
                    .quantity(cart.getCartDetails().get(i).getQuantity())
                    .build();
            list.add(getCartDTO);
        }

        return list;
    }
}
