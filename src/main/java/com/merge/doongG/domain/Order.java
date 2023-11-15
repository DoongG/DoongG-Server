package com.merge.doongG.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Getter
@Builder
@Entity(name = "customer_order")
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    private UUID orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User user;

    private Date orderDate;

    private String orderStatus;

    private String address;
}
