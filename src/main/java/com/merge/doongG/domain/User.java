package com.merge.doongG.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "members")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

}
