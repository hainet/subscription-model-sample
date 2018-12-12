package com.hainet.bank.service;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class DirectDebitEvt {

    private final String id;

    private final int amount;

    private final LocalDateTime dateTime;
}
