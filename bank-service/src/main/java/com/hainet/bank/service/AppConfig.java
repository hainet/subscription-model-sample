package com.hainet.bank.service;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public org.axonframework.config.Configuration configuration() {
        final SubscriptionAccountsProjection subscriptionAccountsProjection = new SubscriptionAccountsProjection();
        final EventHandlingConfiguration eventHandlingConfiguration = new EventHandlingConfiguration();
        eventHandlingConfiguration.registerEventHandler(c -> subscriptionAccountsProjection);

        final org.axonframework.config.Configuration configuration = DefaultConfigurer.defaultConfiguration()
                .configureAggregate(Account.class)
                .configureEventStore(c -> new EmbeddedEventStore(new InMemoryEventStorageEngine()))
                .registerModule(eventHandlingConfiguration)
                .registerQueryHandler(c -> subscriptionAccountsProjection)
                .buildConfiguration();

        configuration.start();

        return configuration;
    }

    @Bean
    public CommandGateway commandGateway(org.axonframework.config.Configuration configuration) {
        return configuration.commandGateway();
    }

    @Bean
    public QueryGateway queryGateway(org.axonframework.config.Configuration configuration) {
        return configuration.queryGateway();
    }
}
