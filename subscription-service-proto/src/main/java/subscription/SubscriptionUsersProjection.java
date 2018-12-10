package subscription;

import billing.BilledEvt;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class SubscriptionUsersProjection {

    private final List<SubscriptionUser> subscriptionUsers = new CopyOnWriteArrayList<>();

    @EventHandler
    public void on(final SubscribedEvt evt) {
        final SubscriptionUser subscriptionUser = new SubscriptionUser(evt.getId(), evt.getUser(), 0);
        this.subscriptionUsers.add(subscriptionUser);
    }

    @EventHandler
    public void on(final BilledEvt evt) {
        this.subscriptionUsers.stream()
                .filter(su -> evt.getId().equals(su.getId()))
                .findFirst()
                .ifPresent(su -> {
                    final SubscriptionUser updatedSubscriptionUser = su.bill(evt.getPrice());
                    subscriptionUsers.remove(su);
                    subscriptionUsers.add(updatedSubscriptionUser);
                });
    }

    @QueryHandler
    public List<SubscriptionUser> fetch(final FetchSubscriptionUsersQuery query) {
        return this.subscriptionUsers.stream()
                .skip(query.getOffset())
                .limit(query.getSize())
                .collect(Collectors.toList());
    }
}
