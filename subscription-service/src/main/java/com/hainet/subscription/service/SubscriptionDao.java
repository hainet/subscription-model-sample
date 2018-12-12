package com.hainet.subscription.service;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionDao {

    private final JdbcTemplate jdbcTemplate;

    public SubscriptionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Subscription findById(final String id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM subscription WHERE id = ?",
                new BeanPropertyRowMapper<>(Subscription.class),
                id
        );
    }

    public void insert(final Subscription subscription) {
        jdbcTemplate.update(
                "INSERT INTO subscription VALUES (?, ?, ?, ?, ?)",
                subscription.getId(),
                subscription.getService(),
                subscription.getAmount(),
                subscription.getUser(),
                subscription.getAccountId()
        );
    }
}
