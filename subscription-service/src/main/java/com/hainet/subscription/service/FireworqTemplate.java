package com.hainet.subscription.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FireworqTemplate {

    private final RestTemplate restTemplate;

    public FireworqTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void subscribe(final Subscription subscription) {
        restTemplate.postForObject("", "", String.class);
    }
}
