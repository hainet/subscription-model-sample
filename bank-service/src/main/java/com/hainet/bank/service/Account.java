package com.hainet.bank.service;

import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;

import java.time.LocalDateTime;

@NoArgsConstructor
public class Account {

    @AggregateIdentifier
    private String id;

    private int balance;

    private LocalDateTime dateTime;

    @CommandHandler
    public Account(final OpenAccountCmd cmd) {
        AggregateLifecycle.apply(new OpenAccountEvt(
                cmd.getId(),
                cmd.getBalance(),
                LocalDateTime.now()
        ));
    }

    @EventSourcingHandler
    public void on(final OpenAccountEvt evt) {
        this.id = evt.getId();
        this.balance = evt.getBalance();
        this.dateTime = evt.getDateTime();
    }

    @CommandHandler
    public void handle(final DirectDebitCmd cmd) {
        AggregateLifecycle.apply(new DirectDebitEvt(
                cmd.getId(),
                cmd.getAmount(),
                LocalDateTime.now()
        ));
    }

    @EventSourcingHandler
    public void on(final DirectDebitEvt evt) {
        this.balance -= evt.getAmount();
        this.dateTime = evt.getDateTime();
    }
}
