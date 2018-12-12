package com.hainet.subscription.service;

import lombok.Data;

@Data
public class Subscription {

    private String id;

    private String service;

    private int amount;

    private String user;

    private String accountId;
}
