package com.hainet.bank.service;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class OpenAccountEvt {

    private final String id;

    private final int balance;

    private final LocalDateTime dateTime;
}
