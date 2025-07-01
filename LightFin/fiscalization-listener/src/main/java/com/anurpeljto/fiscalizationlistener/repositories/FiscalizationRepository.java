package com.anurpeljto.fiscalizationlistener.repositories;

import com.anurpeljto.fiscalizationlistener.domain.Receipt;
import com.anurpeljto.fiscalizationlistener.domain.WeeklyByType;
import com.anurpeljto.fiscalizationlistener.dto.TodayDTO;
import com.anurpeljto.fiscalizationlistener.dto.WeeklyByTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public interface FiscalizationRepository extends JpaRepository<Receipt, Integer> {

    @Query("SELECT r FROM Receipt r WHERE r.timestamp BETWEEN :startOfWeek AND :endOfWeek AND r.status = 'FISCALIZED' order by r.timestamp")
    Page<Receipt> findFiscalizedByThisWeek(
            @Param("startOfWeek") OffsetDateTime startOfWeek,
            @Param("endOfWeek") OffsetDateTime endOfWeek,
            Pageable pageable
    );

    @Query("SELECT r FROM Receipt r WHERE r.timestamp BETWEEN :startOfWeek AND :endOfWeek AND r.status = 'PENDING' order by r.timestamp")
    Page<Receipt> findPendingByThisWeek(
            @Param("startOfWeek") OffsetDateTime startOfWeek,
            @Param("endOfWeek") OffsetDateTime endOfWeek,
            Pageable pageable
    );

    @Query("SELECT r FROM Receipt r WHERE r.timestamp BETWEEN :startOfWeek AND :endOfWeek AND r.status = 'CANCELLED' order by r.timestamp")
    Page<Receipt> findCancelledByThisWeek(
            @Param("startOfWeek") OffsetDateTime startOfWeek,
            @Param("endOfWeek") OffsetDateTime endOfWeek,
            Pageable pageable
    );

    @Query("""
    SELECT new com.anurpeljto.fiscalizationlistener.dto.TodayDTO(r.status, COUNT(r))
    FROM Receipt r
    WHERE r.timestamp BETWEEN :startOfDay AND :endOfDay
    GROUP BY r.status
    ORDER BY r.status
""")
    List<TodayDTO> todaysTransactions(
            @Param("startOfDay") OffsetDateTime startOfDay,
            @Param("endOfDay") OffsetDateTime endOfDay
    );

    @Query("SELECT COUNT(r) FROM Receipt r WHERE r.timestamp BETWEEN :startOfDay AND :endOfDay")
    Integer todaysTransactionsCount(
            @Param("startOfDay") OffsetDateTime startOfDay,
            @Param("endOfDay") OffsetDateTime endOfDay
    );

    @Query("SELECT new com.anurpeljto.fiscalizationlistener.domain.WeeklyByType(r.paymentType, COUNT(r)) " +
            "FROM Receipt r " +
            "WHERE r.timestamp BETWEEN :startOfWeek AND :today " +
            "GROUP BY r.paymentType")
    List<WeeklyByType> weeklyByType(
            @Param("startOfWeek") OffsetDateTime startOfWeek,
            @Param("today") OffsetDateTime today
    );

    @Query("SELECT COUNT(r) FROM Receipt r WHERE r.timestamp BETWEEN :startOfWeek AND :endOfWeek")
    Integer weeklyTransactionsCount(
            @Param("startOfWeek") OffsetDateTime startOfWeek,
            @Param("endOfWeek") OffsetDateTime endOfWeek
    );

    @Query("SELECT COUNT(r) FROM Receipt r WHERE r.timestamp BETWEEN :startOfMonth AND :endOfMonth")
    Integer monthlyTransactionsCount(
            @Param("startOfMonth") OffsetDateTime startOfMonth,
            @Param("endOfMonth") OffsetDateTime endOfMonth
    );

}
