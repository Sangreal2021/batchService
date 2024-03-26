package com.fastcampus.batchcampus.customer;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

// 고객 엔터티 설정

@Entity
@NoArgsConstructor
@Getter
@ToString
public class Customer {

    private Long id;

    private String name;

    private String email;

    private LocalDateTime createAt;

    private LocalDateTime loginAt;

    private Status status;

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
        this.createAt = LocalDateTime.now();
        this.loginAt = LocalDateTime.now();
        this.status = Status.NORMAL;
    }

    public enum Status {
        NORMAL,
        DORMANT;
    }
}
