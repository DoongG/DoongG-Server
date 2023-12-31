package com.merge.doongG.service;

import com.merge.doongG.domain.Order;
import com.merge.doongG.domain.OrderDetail;
import com.merge.doongG.domain.Product;
import com.merge.doongG.domain.User;
import com.merge.doongG.dto.MyOrderDTO;
import com.merge.doongG.dto.MyPageDTO;
import com.merge.doongG.dto.OrderDTO;
import com.merge.doongG.repository.*;
import com.merge.doongG.utils.JwtUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecretKey;
    @Value("${coolsms.api.phone}")
    private String apiPhone;

    private DefaultMessageService messageService;

    @PostConstruct // 생성자 호출 후 자동으로 실행
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
    }

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CartService cartService;
    private final CartRepository cartRepository;

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Value("${jwt.token.secret}")
    private String key;
    private Long expireTimeMs = 1000L * 60 * 60; // 1시간

    private final MailService mailService;


    // 이메일 중복체크
    public boolean emailCheck(String email) {
        Optional<User> selectedUser = userRepository.findByEmail(email);

        return selectedUser.isEmpty() ? true : false;
    }

    // 닉네임 중복 제크
    public boolean nicknameCheck(String nickname) {
        Optional<User> selectedUser = userRepository.findByNickname(nickname);

        return selectedUser.isEmpty() ? true : false;
    }

    // SMS 인증
    public String sendSMS(String phoneNumber) {
        // 해당 전화번호로 이미 가입된 유저가 있는지 확인
        Optional<User> selectedUser = userRepository.findByPhoneNumber(phoneNumber);

        // 이미 가입된 유저가 있는 경우
        if (!selectedUser.isEmpty()) {
            return "false";
        }

        Message message = new Message();

        // 6자리 랜덤 숫자 생성
        String authNumber = String.valueOf((int) (Math.random() * 900000) + 100000);

        message.setFrom(apiPhone);
        message.setTo(phoneNumber);
        message.setText("[둥지] 자취생들의 안식처 둥지의 인증번호는 [" + authNumber + "] 입니다.");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

        return authNumber;
    }

    // 회원가입
    public boolean join(String email, String password, String nickname, String profileImg, String phoneNumber) {
        UUID uuid = UUID.randomUUID(); // 랜덤 uuid 생성

        // 저장
        User user = User.builder()
                .email(email)
                .uuid(uuid)
                .password(bCryptPasswordEncoder.encode(password)) // 암호화해서 저장
                .nickname(nickname)
                .profileImg(profileImg)
                .phoneNumber(phoneNumber)
                .build();

        userRepository.save(user);

        // 유저 회원가입 하면, 해당 유저의 장바구니 생성
        cartService.createCart(user);

        return true;
    }

    // 로그인
    public String login(String email, String password) {

        Optional<User> selectedUser = userRepository.findByEmail(email);

        // 존재하지 않는 회원
        if (selectedUser.isEmpty()) {
            return "0";
        }

        // 비밀번호가 틀린 경우
        if (!bCryptPasswordEncoder.matches(password, selectedUser.get().getPassword())) {
            return "1";
        }

        String str = "";

        String Stoken = JwtUtil.createToken(selectedUser.get().getNickname(), selectedUser.get().getUuid(), key, expireTimeMs);
        String Snickname = selectedUser.get().getNickname();

        str = Stoken + "," + Snickname;

        return str;
    }

    // 이메일 찾기
    public String findEmail(String nickname, String phoneNumber) {
        Optional<User> selectedUser = userRepository.findByNicknameAndPhoneNumber(nickname, phoneNumber);

        // 존재하지 않는 회원
        if (selectedUser.isEmpty()) {
            return "false";
        }

        return selectedUser.get().getEmail();
    }

    // 비밀번호 재설정
    public boolean resetPw(String email, String password) {
        Optional<User> selectedUser = userRepository.findByEmail(email);

        User user = User.builder()
                .id(selectedUser.get().getId())
                .uuid(selectedUser.get().getUuid())
                .email(selectedUser.get().getEmail())
                .password(bCryptPasswordEncoder.encode(password)) // 비밀번호만 새로 설정
                .nickname(selectedUser.get().getNickname())
                .profileImg(selectedUser.get().getProfileImg())
                .phoneNumber(selectedUser.get().getPhoneNumber())
                .reactions(selectedUser.get().getReactions())
                .build();

        userRepository.save(user);

        return true;
    }

    // 이메일 인증
    public String sendEmail(String email) {
        // 해당 이메일로 이미 가입된 유저가 있는지 확인
        Optional<User> selectedUser = userRepository.findByEmail(email);

        // 이미 가입된 유저가 없는 경우
        if (selectedUser.isEmpty()) {
            return "false";
        }

        // 이메일 인증번호 생성
        String code = createCode();

        String title = "[둥지] 비밀번호 재설정 인증번호 안내";
        String content = "자취생들의 안식처 [둥지]의 비밀번호 재설정 인증번호는 [" + code + "] 입니다.";

        mailService.sendEmail(email, title, content);


        return code;
    }

    // 이메일 인증번호 생성
    private String createCode() {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 회원 정보 수정 - 닉네임 변경
    public boolean chNickname(UUID uuid, String nickname) {
        Optional<User> selectedUser = userRepository.findByUuid(uuid);

        User user = User.builder()
                .id(selectedUser.get().getId())
                .uuid(selectedUser.get().getUuid())
                .email(selectedUser.get().getEmail())
                .password(selectedUser.get().getPassword())
                .nickname(nickname)
                .profileImg(selectedUser.get().getProfileImg())
                .phoneNumber(selectedUser.get().getPhoneNumber())
                .reactions(selectedUser.get().getReactions())
                .build();

        userRepository.save(user);

        return true;
    }

    // 회원 정보 수정 - 프로필 이미지 변경
    public boolean chProImg(UUID uuid, String profileImg) {
        Optional<User> selectedUser = userRepository.findByUuid(uuid);

        User user = User.builder()
                .id(selectedUser.get().getId())
                .uuid(selectedUser.get().getUuid())
                .email(selectedUser.get().getEmail())
                .password(selectedUser.get().getPassword())
                .nickname(selectedUser.get().getNickname())
                .profileImg(profileImg)
                .phoneNumber(selectedUser.get().getPhoneNumber())
                .reactions(selectedUser.get().getReactions())
                .build();

        userRepository.save(user);

        return true;
    }

    // 회원 정보 수정 - 비밀번호 변경
    public boolean chPw(UUID uuid, String password) {
        Optional<User> selectedUser = userRepository.findByUuid(uuid);

        User user = User.builder()
                .id(selectedUser.get().getId())
                .uuid(selectedUser.get().getUuid())
                .email(selectedUser.get().getEmail())
                .password(bCryptPasswordEncoder.encode(password)) //
                .nickname(selectedUser.get().getNickname())
                .profileImg(selectedUser.get().getProfileImg())
                .phoneNumber(selectedUser.get().getPhoneNumber())
                .reactions(selectedUser.get().getReactions())
                .build();

        userRepository.save(user);

        return true;
    }

    public MyPageDTO myPage(UUID uuid) {
        // 해당 uuid로 유저 찾기
        Optional<User> selectedUser = userRepository.findByUuid(uuid);

        MyPageDTO myPageDTO = MyPageDTO.builder()
                .nickname(selectedUser.get().getNickname())
                .profileImg(selectedUser.get().getProfileImg())
                .email(selectedUser.get().getEmail())
                .build();

        return myPageDTO;
    }

    // 상품 주문
    public String order(UUID uuid, OrderDTO dto) {
        // uuid로 유저 찾기
        Optional<User> selectedUser = userRepository.findByUuid(uuid);

        // productID로 상품 찾기
        Optional<Product> selectedProduct = shopRepository.findByProductID(dto.getProductId());

        OrderDetail orderDetail = OrderDetail.builder()
                .product(selectedProduct.get())
                .build();

        orderDetailRepository.save(orderDetail);

        Order order = Order.builder()
                .user(selectedUser.get())
                .orderDetail(orderDetail)
                .postcode(dto.getPostcode())
                .orderStatus("결제완료")
                .address(dto.getAddress())
                .quantity(dto.getQuantity())
                .build();

        orderRepository.save(order);

        return "true";
    }

    // 주문내역 조회
    public List<MyOrderDTO> myOrder(UUID uuid) {
        // 해당 uuid로 유저 찾기
        Optional<User> selectedUser = userRepository.findByUuid(uuid);

        // 해당 유저의 id가 order의 customer_id와 같으므로 해당 유저의 id로 order 찾기
        Long userId = selectedUser.get().getId();

        List<Order> orderList = orderRepository.findByUserId(userId);
        List<MyOrderDTO> myOrderDTOList = new ArrayList<>();

        for (Order order : orderList) {
            MyOrderDTO myOrderDTO = MyOrderDTO.builder()
                    .orderId(order.getOrderId())
                    .orderDate(order.getOrderDate().toString())
                    .orderStatus(order.getOrderStatus())
                    .postcode(order.getPostcode())
                    .address(order.getAddress())
                    .quantity(order.getQuantity())
                    .productName(order.getOrderDetail().getProduct().getProductName())
                    .productImg(order.getOrderDetail().getProduct().getProductImage())
                    .productPrice(order.getOrderDetail().getProduct().getPrice())
                    .productDiscountPrice(order.getOrderDetail().getProduct().getDiscountedPrice())
                    .build();

            myOrderDTOList.add(myOrderDTO);
        }

        return myOrderDTOList;
    }
}