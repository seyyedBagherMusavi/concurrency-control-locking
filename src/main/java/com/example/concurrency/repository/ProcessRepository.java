package com.example.concurrency.repository;

import com.example.concurrency.model.Process;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
        @QueryHint(name = "javax.persistence.lock.timeout", value = "-2") // Skip locked rows
    })
    @Query("SELECT p FROM Process p WHERE p.processed = false")
    List<Process> findUnprocessedWithLock(Pageable pageable);
}
