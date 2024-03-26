package com.fastcampus.batchcampus;

import com.fastcampus.batchcampus.customer.CustomerRepository;
import org.springframework.stereotype.Component;

@Component
public class DormantBatchJob {

    private final CustomerRepository customerRepository;
    private final EmailProvider emailProvider;

    public DormantBatchJob(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.emailProvider = new EmailProvider.Fake();
    }

    // 배치 실행
    public void execute(){

        // 1. 유저를 조회

        // 2. 휴면계정 대상을 추출 및 변환

        // 3. 휴면계정으로 상태를 변경

        // 4. 메일을 보냄
    }
}
