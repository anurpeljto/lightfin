package com.anurpeljto.fiscalizationlistener.repositories;

import com.anurpeljto.fiscalizationlistener.domain.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public interface FiscalizationRepository extends JpaRepository<Receipt, Integer> {

    @Query("SELECT r FROM Receipt r WHERE r.timestamp BETWEEN :startOfWeek AND :endOfWeek AND r.status = 'FISCALIZED'")
    List<Receipt> findFiscalizedByThisWeek(
            @Param("startOfWeek") OffsetDateTime startOfWeek,
            @Param("endOfWeek") OffsetDateTime endOfWeek
    );
}
