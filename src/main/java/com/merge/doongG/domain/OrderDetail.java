package com.merge.doongG.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailID;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
