package com.example.concurrency.service;

import com.example.concurrency.model.Process;
import com.example.concurrency.repository.ProcessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessService {

    private final ProcessRepository processRepository;

    @Transactional
    @Async // Enables parallel processing
    public CompletableFuture<Void> processBatch() {
        List<Process> tasks = processRepository.findUnprocessedWithLock(PageRequest.of(0, 10));

        if (tasks.isEmpty()) {
            log.info("No tasks left to process.");
            return CompletableFuture.completedFuture(null);
        }

        tasks.forEach(task -> {
            try {
                Thread.sleep(500); // Simulate processing delay
                task.setProcessed(true);
                processRepository.save(task);
                log.info("Processed task ID: {}", task.getId());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        return CompletableFuture.completedFuture(null);
    }
}
