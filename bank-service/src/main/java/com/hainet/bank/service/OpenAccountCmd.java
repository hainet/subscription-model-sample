package com.hainet.bank.service;

import lombok.Value;

@Value
public class OpenAccountCmd {

    private String id;

    private int balance;
}
