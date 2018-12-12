package com.hainet.subscription.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class BankServiceTemplate {

    private final RestTemplate restTemplate;

    public BankServiceTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void directDebit(final String accountId, final int amount) {
        final Map<String, Object> directDebitRequest = new HashMap<>();
        directDebitRequest.put("id", accountId);
        directDebitRequest.put("amount", amount);

        restTemplate.postForObject(
                "http://localhost:9020/direct-debit",
                directDebitRequest,
                String.class
        );
    }
}
