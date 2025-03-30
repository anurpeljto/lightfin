package com.anurpeljto.loanlistener.repositories;

import com.anurpeljto.loanlistener.domain.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Integer> {
}
