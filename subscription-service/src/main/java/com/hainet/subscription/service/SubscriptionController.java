package com.hainet.subscription.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
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

    public void subscribe(@RequestBody final Subscription subscription) {
        System.out.println(subscription);

        // 口座照会クエリ
        bankServiceTemplate.checkAccount();

        // サブスクリプション情報書き込み
        dao.insert(subscription);

        // 継続課金キュー
        fireworqTemplate.subscribe(subscription);
    }

    @PostMapping("/bill/{subscriptionId}")
    public void bill(@PathVariable final String subscriptionId) {
        System.out.println(subscriptionId);

        // サブスクリプション情報取得
        final Subscription subscription = dao.findById(subscriptionId);

        // 引き落としコマンド
        bankServiceTemplate.directDebit();

        // 継続課金キュー
        fireworqTemplate.subscribe(subscription);
    }
}
