package com.anurpeljto.fiscalizationlistener.repositories;

import com.anurpeljto.fiscalizationlistener.domain.Receipt;
import com.anurpeljto.fiscalizationlistener.dto.TodayDTO;
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

    @Query("SELECT r FROM Receipt r WHERE r.timestamp BETWEEN :startOfWeek AND :endOfWeek AND r.status = 'PENDING'")
    List<Receipt> findPendingByThisWeek(
            @Param("startOfWeek") OffsetDateTime startOfWeek,
            @Param("endOfWeek") OffsetDateTime endOfWeek
    );

    @Query("SELECT r FROM Receipt r WHERE r.timestamp BETWEEN :startOfWeek AND :endOfWeek AND r.status = 'CANCELLED'")
    List<Receipt> findCancelledByThisWeek(
            @Param("startOfWeek") OffsetDateTime startOfWeek,
            @Param("endOfWeek") OffsetDateTime endOfWeek
    );

    @Query("SELECT new com.anurpeljto.fiscalizationlistener.dto.TodayDTO(r.status, COUNT(r)) FROM Receipt r WHERE r.timestamp BETWEEN :startOfDay AND :endOfDay GROUP BY r.status")
    List<TodayDTO> todaysTransactions(
            @Param("startOfDay") OffsetDateTime startOfDay,
            @Param("endOfDay") OffsetDateTime endOfDay
    );
}
