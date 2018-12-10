import billing.BillingCmd;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configuration;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.responsetypes.ResponseTypes;
import subscription.FetchSubscriptionUsersQuery;
import subscription.SubscriptionCmd;
import subscription.SubscriptionUser;
import subscription.SubscriptionUsersProjection;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(final String[] args) throws ExecutionException, InterruptedException {
        final SubscriptionUsersProjection subscriptionUsersProjection = new SubscriptionUsersProjection();
        final EventHandlingConfiguration eventHandlingConfiguration = new EventHandlingConfiguration();
        eventHandlingConfiguration.registerEventHandler(c -> subscriptionUsersProjection);

        final Configuration configuration = DefaultConfigurer
                .defaultConfiguration()
                .configureAggregate(KujakuSubscription.class)
                .configureEventStore(c -> new EmbeddedEventStore(new InMemoryEventStorageEngine()))
                .registerModule(eventHandlingConfiguration)
                .registerQueryHandler(c -> subscriptionUsersProjection)
                .buildConfiguration();

        configuration.start();

        final CommandGateway commandGateway = configuration.commandGateway();
        final QueryGateway queryGateway = configuration.queryGateway();

        final String hainet = UUID.randomUUID().toString();
        commandGateway.sendAndWait(new SubscriptionCmd(hainet, "hainet", "BRONZE"));
        commandGateway.sendAndWait(new BillingCmd(hainet, 9800));

        final String kujaku = UUID.randomUUID().toString();
        commandGateway.sendAndWait(new SubscriptionCmd(kujaku, "kujaku", "GOLD"));
        commandGateway.sendAndWait(new BillingCmd(kujaku, 29800));
        commandGateway.sendAndWait(new BillingCmd(kujaku, 29800));
        commandGateway.sendAndWait(new BillingCmd(kujaku, 29800));

        queryGateway.query(new FetchSubscriptionUsersQuery(10, 0), ResponseTypes.multipleInstancesOf(SubscriptionUser.class))
                .get()
                .forEach(System.out::println);
    }
}
