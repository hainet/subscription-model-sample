package com.hainet.bank.service;

import lombok.Value;

@Value
public class FetchSubscriptionAccountQuery {

    private final Integer size;

    private final Integer offset;
}
