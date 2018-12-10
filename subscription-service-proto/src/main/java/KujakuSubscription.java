import billing.BilledEvt;
import billing.BillingCmd;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import subscription.SubscribedEvt;
import subscription.SubscriptionCmd;

@NoArgsConstructor
public class KujakuSubscription {

    @AggregateIdentifier
    private String id;

    private String user;

    private String plan;

    private Integer billed;

    @CommandHandler
    public KujakuSubscription(final SubscriptionCmd cmd) {
        AggregateLifecycle.apply(new SubscribedEvt(
                cmd.getId(),
                cmd.getUser(),
                cmd.getPlan()
        ));
    }

    @EventSourcingHandler
    public void on(final SubscribedEvt evt) {
        this.id = evt.getId();
        this.user = evt.getUser();
        this.plan = evt.getPlan();
        this.billed = 0;
    }

    @CommandHandler
    public void handle(final BillingCmd cmd) {
        AggregateLifecycle.apply(new BilledEvt(
                cmd.getId(),
                cmd.getPrice()
        ));
    }

    @EventSourcingHandler
    public void on(final BilledEvt evt) {
        this.billed += evt.getPrice();
    }
}
