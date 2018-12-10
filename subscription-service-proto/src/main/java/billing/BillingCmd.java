package billing;

import lombok.Value;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Value
public class BillingCmd {

    @TargetAggregateIdentifier
    private final String id;

    private final Integer price;
}
