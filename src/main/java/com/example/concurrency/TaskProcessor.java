package com.example.concurrency;

import com.example.concurrency.service.ProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskProcessor implements CommandLineRunner {

    private final ProcessService processService;

    @Override
    public void run(String... args) {
        log.info("Starting parallel task processing...");

        List<CompletableFuture<Void>> futures = IntStream.range(0, 10)
                .mapToObj(i -> processService.processBatch())
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        log.info("Processing complete.");
    }
}
