package com.fastcampus.batchcampus;

import com.fastcampus.batchcampus.batch.BatchStatus;
import com.fastcampus.batchcampus.batch.JobExecution;
import com.fastcampus.batchcampus.customer.Customer;
import com.fastcampus.batchcampus.customer.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DormantBatchJob {

    private final CustomerRepository customerRepository;
    private final EmailProvider emailProvider;

    public DormantBatchJob(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.emailProvider = new EmailProvider.Fake();
    }

    // 배치 실행
    public JobExecution execute(){

        final JobExecution jobExecution = new JobExecution();
        jobExecution.setStatus(BatchStatus.STARTING);
        jobExecution.setStartTime(LocalDateTime.now());

        int pageNo = 0;

        try {
            while (true) {

                // 1. 유저를 조회
                // PageRequest 객체를 생성하여 페이지 번호(pageNo), 페이지 당 항목 수(여기서는 1),
                // 그리고 ID를 기준으로 오름차순 정렬하는 방식을 지정함. 이는 한 번에 하나의 고객 데이터만 처리하도록 함.
                final PageRequest pageRequest = PageRequest.of(pageNo, 1, Sort.by("id").ascending());
                final Page<Customer> page = customerRepository.findAll(pageRequest);

                final Customer customer;
                // 조회된 페이지가 비어 있으면 더 이상 처리할 고객이 없으므로 루프를 종료
                if (page.isEmpty()) {
                    break;
                } else {
                    // 페이지가 비어 있지 않다면 페이지 번호를 증가시키고,
                    // 페이지에서 첫 번째 고객 데이터를 가져와 customer 변수에 할당
                    pageNo++;
                    customer = page.getContent().get(0);
                }

                // 2. 휴면계정 대상을 추출 및 변환
                // 로그인 날짜      /      365일 전      /      오늘
                // 현재 날짜에서 365일을 뺀 날짜가 고객의 마지막 로그인 날짜보다 이후인지 확인.
                final boolean isDormantTarget = LocalDate.now()
                        .minusDays(365)
                        .isAfter(customer.getLoginAt().toLocalDate());

                if (isDormantTarget) {
                    customer.setStatus(Customer.Status.DORMANT);
                } else {
                    continue;
                }

                // 3. 휴면계정으로 상태를 변경
                customerRepository.save(customer);

                // 4. 메일을 보냄
                emailProvider.send(customer.getEmail(), "휴면전환 안내메일입니다.", "휴면 전환됨");
            }
            jobExecution.setStatus(BatchStatus.COMPLETED);
        } catch (Exception e) {
            jobExecution.setStatus(BatchStatus.FAILED);
        }
        jobExecution.setEndTime(LocalDateTime.now());

        emailProvider.send(
                "admin@fastcampus.com",
                "배치 완료 알림",
                "DormantBatchJob이 수행되었습니다. status :" + jobExecution.getStatus()
        );

        return jobExecution;
    }
}
