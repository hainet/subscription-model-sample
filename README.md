# TimeFuze Architecture

## Origin
https://blog.yuuk.io/entry/2017/timefuze-architecture

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
