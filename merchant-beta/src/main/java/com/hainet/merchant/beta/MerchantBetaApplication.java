package com.hainet.merchant.beta;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
@Slf4j
public class MerchantBetaApplication {

    private final JdbcTemplate jdbcTemplate;

    private final RestTemplate restTemplate;

    public MerchantBetaApplication(
            final JdbcTemplate jdbcTemplate,
            final RestTemplate restTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.restTemplate = restTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(MerchantBetaApplication.class, args);
    }

    @PostMapping("")
    public String process(@RequestBody final Job job) {
        // Process your job!

        log.info("Success! Job: {}", job);

        jdbcTemplate.update(
                "INSERT INTO job (merchant, job_id, count, cycle, status, created_at) VALUES (?, ?, ?, ?, ?, NOW())",
                "beta",
                job.getId(),
                job.getCount(),
                job.getCycle(),
                "success"
        );

        job.setCount(job.getCount() + 1);

        restTemplate.postForObject(
                "/job/merchant-beta",
                new FireworqMessage("http://Haines-MBP.elecom:8092", job.getCycle(), job),
                String.class
        );

        return "{\"status\": \"success\"}";
    }

    @Data
    private static class Job {

        private String id;

        private Integer count;

        private Integer cycle;

        private Detail detail;

        @Data
        private static class Detail {

            private String name;

            private String product;

            private Integer amount;
        }
    }

    @Value
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    private static class FireworqMessage {

        private String url;

        private int runAfter;

        private Job payload;
    }
}
