package com.merge.doongG.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Getter
@Builder
@Entity(name = "customer_order")
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User user;

    // OrderDetail 연결
    @OneToOne
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    // 주문일자 자동생성
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date orderDate;

    // 주문상태
    @Column(nullable = false)
    private String orderStatus;

    // 우편번호
    @Column(nullable = false)
    private String postcode;

    // 상세주소
    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private int quantity; // 주문수량
}