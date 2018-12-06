package com.hainet.failure.cleaner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
public class FailureCleanerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FailureCleanerApplication.class, args);
    }

    @Scheduled(cron = "* * * * * 0")
    public void toMySQL() {
        // /queue/default/failedをMySQLに移行する。
    }

    @Scheduled(cron = "* * * * * 0")
    public void toFireworq() {
        // SELECT * FROM job WHERE status = 'failure'をfireworqに再びキューイングする。
    }
}
