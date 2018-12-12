package com.hainet.bank.service;

import lombok.Value;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Value
public class DirectDebitCmd {

    @TargetAggregateIdentifier
    private final String id;

    private final int amount;
}
