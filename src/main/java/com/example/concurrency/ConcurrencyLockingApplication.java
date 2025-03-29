package com.example.concurrency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // Enables parallel execution
public class ConcurrencyLockingApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConcurrencyLockingApplication.class, args);
    }
}
