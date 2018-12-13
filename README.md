# 継続課金システム
![](http://drive.google.com/uc?export=view&id=1BFnCZJzNgy4EamkhI_wYn4HV1ckrgB0g)

## コンポーネント

### 継続課金サービス

継続課金登録を受け付け、1ヶ月に1回課金処理を行う。

|エンドポイント|概要|
|:---:|:---|
|/subscribe|継続課金を登録する。|

### 銀行サービス

|エンドポイント|概要|
|:---:|:---|
|/check-account|口座の開設状況を確認する。|
|/open-account|口座を開設する。|
|/check-balance|口座の残高を確認する。|
|/direct-debit|口座から任意の金額を引き落としする。|

操作の名称をエンドポイント名とし、RESTらしい名前を敢えて避けている。

## アーキテクチャ

- TimeFuze Architecture  
https://blog.yuuk.io/entry/2017/timefuze-architecture  
- CQRS
- イベントソーシング

## 技術スタック

- Spring Boot
- Axon Framework
- Axon DB
- H2 Database
- Fireworq

## Cheat sheet

### fireworq

https://github.com/fireworq/fireworq
```bash
script/docker/compose up
```

### 継続課金サービス

- サブスクリプション登録(エンドユーザーが利用する)
```bash
curl -X POST http://localhost:9010/subscribe -H 'Content-Type:application/json' -d '{"service":"サブスクリプション","amount":980,"user":"hainet","account_id":"UUID"}'
```

- 課金(継続課金サービスがFireworq経由で利用する)
```bash
curl -X POST http://localhost:9010/bill/{subscriptionId}
```

### 銀行サービス

- 口座開設(エンドユーザーが利用する)
```bash
curl -X POST http://localhost:9020/open-account -H 'Content-Type:application/json' -d '{"balance":500000}'
```

- 口座引き落とし(継続課金サービスが利用する)
```bash
curl -X POST http://localhost:9020/direct-debit -H 'Content-Type:application/json' -d '{"id":"UUID","amount":980}'
```

- 口座照会(デバッグ用)
```bash
curl http://localhost:9020/accounts
```
