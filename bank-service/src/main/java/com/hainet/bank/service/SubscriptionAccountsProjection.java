package com.hainet.bank.service;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class SubscriptionAccountsProjection {

    private final List<SubscriptionAccount> subscriptionAccounts = new CopyOnWriteArrayList<>();

    @EventHandler
    public void on(final OpenAccountEvt evt) {
        this.subscriptionAccounts.add(new SubscriptionAccount(
                evt.getId(),
                evt.getBalance(),
                evt.getDateTime()
        ));
    }

    @EventHandler
    public void on(final DirectDebitEvt evt) {
        this.subscriptionAccounts.stream()
                .filter(sa -> evt.getId().equals(sa.getId()))
                .findFirst()
                .ifPresent(sa -> {
                    final SubscriptionAccount updatedSubscriptionAccount = sa.directDebit(evt.getAmount());
                    subscriptionAccounts.remove(sa);
                    subscriptionAccounts.add(updatedSubscriptionAccount);
                });
    }

    @QueryHandler
    public List<SubscriptionAccount> fetch(final FetchSubscriptionAccountQuery query) {
        return this.subscriptionAccounts.stream()
                .skip(query.getOffset())
                .limit(query.getSize())
                .collect(Collectors.toList());
    }
}
