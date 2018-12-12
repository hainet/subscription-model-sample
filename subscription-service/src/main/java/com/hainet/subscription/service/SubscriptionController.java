package com.hainet.subscription.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
public class SubscriptionController {

    private final BankServiceTemplate bankServiceTemplate;

    private final SubscriptionDao dao;

    private final FireworqTemplate fireworqTemplate;

    public SubscriptionController(
            final BankServiceTemplate bankServiceTemplate,
            final SubscriptionDao dao,
            final FireworqTemplate fireworqTemplate) {
        this.bankServiceTemplate = bankServiceTemplate;
        this.dao = dao;
        this.fireworqTemplate = fireworqTemplate;
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestBody final Subscription subscription) {
        final String subscriptionId = UUID.randomUUID().toString();
        subscription.setId(subscriptionId);

        // サブスクリプション情報書き込み
        dao.insert(subscription);

        // 継続課金キュー
        fireworqTemplate.subscribe(subscription);

        return subscriptionId;
    }

    @PostMapping("/bill/{subscriptionId}")
    public String bill(@PathVariable final String subscriptionId) {
        // サブスクリプション情報取得
        final Subscription subscription = dao.findById(subscriptionId);

        if (subscription != null) {
            // 引き落としコマンド
            bankServiceTemplate.directDebit(
                    subscription.getAccountId(),
                    subscription.getAmount()
            );

            // 継続課金キュー
            fireworqTemplate.subscribe(subscription);

            log.info("Receipt here: " + subscription);

            return "{\"status\":\"success\"}";
        } else {
            throw new RuntimeException();
        }
    }
}
