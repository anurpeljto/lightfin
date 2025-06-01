package com.anurpeljto.loanlistener.repositories;

import com.anurpeljto.loanlistener.domain.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Integer> {

    Page<Loan> findByBorrowerId(
            Integer id,
            Pageable pageable
    );
}
