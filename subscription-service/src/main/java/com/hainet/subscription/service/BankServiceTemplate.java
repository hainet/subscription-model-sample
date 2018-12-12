package com.hainet.subscription.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BankServiceTemplate {

    private final RestTemplate restTemplate;

    public BankServiceTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void checkAccount() {

    }

    public void directDebit() {

    }
}
