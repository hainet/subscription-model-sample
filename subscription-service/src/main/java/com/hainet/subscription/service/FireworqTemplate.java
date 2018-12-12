package com.hainet.subscription.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FireworqTemplate {

    private final RestTemplate restTemplate;

    public FireworqTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void subscribe(final Subscription subscription) {
        restTemplate.postForObject(
                "http://localhost:8080/job/subscription",
                new FireworqMessage(
                        "http://Haines-MBP.elecom:9010/bill/" + subscription.getId(),
                        3
                ),
                String.class
        );
    }

    @Value
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    private static class FireworqMessage {

        private String url;

        private int runAfter;
    }
}
