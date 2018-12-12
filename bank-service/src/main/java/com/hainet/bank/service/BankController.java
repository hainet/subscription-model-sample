package com.hainet.bank.service;

import lombok.Data;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.responsetypes.ResponseTypes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
public class BankController {

    private final CommandGateway commandGateway;

    private final QueryGateway queryGateway;

    public BankController(
            final CommandGateway commandGateway,
            final QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping("/open-account")
    public String openAccount(@RequestBody final OpenAccountRequest request) {
        final String accountId = UUID.randomUUID().toString();

        commandGateway.sendAndWait(new OpenAccountCmd(accountId, request.getBalance()));

        return accountId;
    }

    @Data
    public static class OpenAccountRequest {

        private int balance;
    }

    @PostMapping("/direct-debit")
    public void directDebit(@RequestBody final DirectDebitRequest request) {
        System.out.println(request);

        commandGateway.sendAndWait(new DirectDebitCmd(request.getId(), request.getAmount()));
    }

    @Data
    public static class DirectDebitRequest {

        private String id;

        private int amount;
    }

    @GetMapping("/accounts")
    public List<SubscriptionAccount> accounts() throws ExecutionException, InterruptedException {
        return queryGateway
                .query(
                        new FetchSubscriptionAccountQuery(10, 0),
                        ResponseTypes.multipleInstancesOf(SubscriptionAccount.class)
                )
                .get();
    }
}
