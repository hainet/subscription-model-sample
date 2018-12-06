package com.hainet.merchant.gamma;

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

import java.util.Random;

@SpringBootApplication
@RestController
@Slf4j
public class MerchantGammaApplication {

    private final JdbcTemplate jdbcTemplate;

    private final RestTemplate restTemplate;

    public MerchantGammaApplication(
            final JdbcTemplate jdbcTemplate,
            final RestTemplate restTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.restTemplate = restTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(MerchantGammaApplication.class, args);
    }

    @PostMapping("")
    public String process(@RequestBody final Job job) {
        // Process your job!

        if (new Random().nextInt(100) > 90) {
            log.info("Success! Job: {}", job);

            jdbcTemplate.update(
                    "INSERT INTO job (merchant, job_id, count, cycle, status, created_at) VALUES (?, ?, ?, ?, ?, NOW())",
                    "alpha",
                    job.getId(),
                    job.getCount(),
                    job.getCycle(),
                    "success"
            );

            job.setCount(job.getCount() + 1);

            restTemplate.postForObject(
                    "/job/merchant-gamma",
                    new FireworqMessage(
                            "http://Haines-MBP.elecom:8093",
                            job.getCycle(),
                            job.getMaxRetries(),
                            job.getRetryDelay(),
                            job),
                    String.class
            );

            return "{\"status\": \"success\"}";
        } else {
            log.error("Failure! Job: {}", job);

            return "{\"status\": \"failure\"}";
        }
    }

    @Data
    private static class Job {

        private String id;

        private Integer count;

        private Integer cycle;

        private int maxRetries;

        private int retryDelay;

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

        private int maxRetries;

        private int retryDelay;

        private Job payload;
    }
}
