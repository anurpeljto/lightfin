package com.anurpeljto.subsidylistener.repositories;

import com.anurpeljto.subsidylistener.domain.Subsidy;
import com.anurpeljto.subsidylistener.model.SubsidyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

public interface SubsidyRepository extends JpaRepository<Subsidy, Integer> {
    Page<Subsidy> findByRecipientId(Integer id, Pageable pageable);

    Page<Subsidy> findByStatus(SubsidyStatus status, Pageable pageable);

    @Query("SELECT s FROM Subsidy s WHERE s.timestamp BETWEEN :startOfWeek AND :endOfWeek")
    Page<Subsidy> getWeeklySubsidies(
            @Param("startOfWeek") OffsetDateTime startOfWeek,
            @Param("endOfWeek") OffsetDateTime endOfWeek,
            Pageable pageable
    );
}
