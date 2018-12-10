package billing;

import lombok.Value;

@Value
public class BilledEvt {

    private final String id;

    private final Integer price;
}
