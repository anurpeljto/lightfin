package com.anurpeljto.gateway.repositories.loan;

import com.anurpeljto.gateway.domain.loan.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface LoanRepository extends JpaRepository<Loan, Integer> {
}
