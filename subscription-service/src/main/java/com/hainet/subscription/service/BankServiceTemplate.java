package com.hainet.subscription.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BankServiceTemplate {

    private final RestTemplate restTemplate;

    public BankServiceTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void checkAccount(final String accountId) {
        restTemplate.getForObject(
                "http://localhost:9020/check-accountId?account_id={accountId}",
                String.class,
                accountId
        );
    }

    public void directDebit() {
        restTemplate.postForObject(
                "http://localhost:9020/direct-debit",
                "",
                String.class
        );
    }
}
