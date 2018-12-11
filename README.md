# 継続課金システム
![](http://drive.google.com/uc?export=view&id=1etbrE32uSQvXpF2XcohhtYg3Z4EA8jLu)

## コンポーネント

### 継続課金システム
継続課金登録を受け付け、1ヶ月に1回課金処理を行う。

|エンドポイント|概要|
|:---:|:---|
|/subscribe|継続課金を登録する。|

### 銀行システム

|エンドポイント|概要|
|:---:|:---|
|/open-account|口座を開設する。|
|/direct-debit|口座から任意の金額を引き落としする。|
|/check-balance|継続課金登録をする。|

操作の名称をエンドポイント名とし、RESTらしい名前を敢えて避けている。

## アーキテクチャ

- TimeFuze Architecture  
https://blog.yuuk.io/entry/2017/timefuze-architecture  
- CQRS
- イベントソーシング

## ↓Delete later↓

## How to use it

### fireworq
https://github.com/fireworq/fireworq
```bash
script/docker/compose up
```

### mysql
```bash
mysql.server start
mysql -u root
```
```mysql
CREATE DATABASE timefuze_architecture;

SHOW DATABASES;

USE timefuze_architecture;

CREATE TABLE job (
    id INT AUTO_INCREMENT PRIMARY KEY,
    merchant VARCHAR(5) NOT NULL,
    job_id VARCHAR(36) NOT NULL,
    count INT NOT NULL,
    cycle INT NOT NULL,
    status VARCHAR(10) NOT NULL,
    created_at DATETIME NOT NULL,
    UNIQUE(job_id, count)
);

DESCRIBE job;
```

### Job queue
```bash
curl -XPOST \
http://localhost:8080/job/merchant-alpha \
-d \
'{
  "url": "http://Haines-MBP.elecom:8091",
  "payload": {
      "id": "プロテイン定期購入",
      "count": 1,
      "cycle": 3,
      "detail": {
          "name": "haient",
          "product": "プロテイン",
          "amount": 4980
      }
  }
}'

curl -XPOST \
http://localhost:8080/job/merchant-alpha \
-d \
'{
  "url": "http://Haines-MBP.elecom:8091",
  "payload": {
      "id": "マルチビタミン定期購入",
      "count": 1,
      "cycle": 2,
      "detail": {
          "name": "haient",
          "product": "マルチビタミン",
          "amount": 1280
      }
  }
}'

curl -XPOST \
http://localhost:8080/job/merchant-beta \
-d \
'{
    "url": "http://Haines-MBP.elecom:8092",
    "payload": {
        "id": "スプラトゥーン継続課金",
        "count": 1,
        "cycle": 1,
        "detail": {
            "name": "yama",
            "product": "スプラトゥーン",
            "amount": 980
        }
    }
}'

curl -XPOST \
http://localhost:8080/job/merchant-gamma \
-d \
'{
    "url": "http://Haines-MBP.elecom:8093",
    "max_retries": 3,
    "retry_delay": 3,
    "payload": {
        "id": "ブレードサーバー維持費",
        "count": 1,
        "cycle": 12,
        "max_retries": 3,
        "retry_delay": 3, 
        "detail": {
            "name": "mae",
            "product": "ブレードサーバー",
            "amount": 980000
        }
    }
}'
```

### Job management
```bash
curl localhost:8080/queue/default/waiting | jq
curl localhost:8080/queue/default/failed | jq
```
