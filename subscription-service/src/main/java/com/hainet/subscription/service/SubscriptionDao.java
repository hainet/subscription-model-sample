package com.hainet.subscription.service;

import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionDao {

    public Subscription findById(final String id) {
        return new Subscription();
    }

    public int insert(final Subscription subscription) {
        return 1;
    }
}
