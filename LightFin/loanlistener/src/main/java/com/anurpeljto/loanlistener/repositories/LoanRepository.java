package com.anurpeljto.loanlistener.repositories;

import com.anurpeljto.loanlistener.domain.Loan;
import com.anurpeljto.loanlistener.model.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Integer> {

    Page<Loan> findByBorrowerId(
            Integer id,
            Pageable pageable
    );

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.timestamp BETWEEN :startOfWeek AND :endOfWeek")
    Long getLoansThisWeek(
            @Param("startOfWeek")OffsetDateTime startOfWeek,
            @Param("endOfWeek") OffsetDateTime endOfWeek
    );

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.status = :status")
    Long getPendingLoans(
            @Param("status")LoanStatus status
    );

    @Query("SELECT AVG(l.amount) FROM Loan l")
    Double getAverageLoanAmount();
}
