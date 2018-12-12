package com.hainet.bank.service;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class SubscriptionAccount {

    private final String id;

    private final int balance;

    private final LocalDateTime dateTime;

    public SubscriptionAccount directDebit(final int amount) {
        return new SubscriptionAccount(this.id, this.balance - amount, this.dateTime);
    }
}
