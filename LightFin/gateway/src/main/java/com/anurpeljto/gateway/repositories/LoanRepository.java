package com.anurpeljto.gateway.repositories;

import com.anurpeljto.gateway.domain.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Integer> {
}
