package com.fastcampus.batchcampus;

import com.fastcampus.batchcampus.customer.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DormantBatchJobTest {

    @Test
    @DisplayName("로그인 시간이 일년을 경과한 고객이 세명이고, 일년 이내에 로그인한 고객이 다섯명이면 3명의 고객이 휴면전환대상이다.")
    void test1(){

        new Customer("test", "test@fastcampus.com");
    }

    @Test
    @DisplayName("고객이 열명이 있지만 모두 다 휴면전환대상이 아니면 휴면전환 대상은 0명이다.")
    void test2() {

    }

    @Test
    @DisplayName("고객이 없는 경우에도 배치는 정상동작해야 한다.")
    void test() {

    }
}