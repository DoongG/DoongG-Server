package com.merge.doongG.service;

import com.merge.doongG.domain.Cart;
import com.merge.doongG.domain.CartDetail;
import com.merge.doongG.domain.Product;
import com.merge.doongG.domain.User;
import com.merge.doongG.dto.GetCartDTO;
import com.merge.doongG.repository.CartRepository;
import com.merge.doongG.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ShopRepository productRepository;

    @Override
    public void createCart(User user) {
        // 장바구니 생성
        Cart cart = Cart.builder()
                .user(user)
                .build();

        cartRepository.save(cart);
    }

    @Override
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
                    .createdAt(cart.getCartDetails().get(i).getCreatedAt().toString())
                    .build();
            list.add(getCartDTO);
        }

        // 생성일자 기준으로 최신순 정렬
        list.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));

        return list;
    }

    @Override
    public String addCart(UUID uuid, Long productID, int quantity) {
        // uuid로 장바구니 찾기
        Cart cart = cartRepository.findByUserUuid(uuid);

        // productID로 상품 찾기
        Optional<Product> product = productRepository.findByProductID(productID);

        // 장바구니에 상품 추가
        CartDetail cartDetail = CartDetail.builder()
                .cart(cart)
                .product(product.get())
                .quantity(quantity)
                .build();

        cart.getCartDetails().add(cartDetail);

        cartRepository.save(cart);

        return "true";
    }
}
