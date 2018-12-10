package subscription;

import lombok.Value;

@Value
public class SubscriptionUser {

    private final String id;

    private final String user;

    private final Integer billed;

    public SubscriptionUser(final String id, final String user, final Integer billed) {
        this.id = id;
        this.user = user;
        this.billed = billed;
    }

    public SubscriptionUser bill(final Integer price) {
        return new SubscriptionUser(this.id, this.user, this.billed + price);
    }
}
