package subscription;

import lombok.Value;

@Value
public class FetchSubscriptionUsersQuery {

    private final Integer size;

    private final Integer offset;
}
