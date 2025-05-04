package com.anurpeljto.gateway.services;

import com.anurpeljto.gateway.domain.loan.Loan;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LoanService {

    Optional<Loan> getLoanById(Integer id);

    void approveLoan(Loan loan);

    void rejectLoan(Loan loan);

    void publishLoan(Loan loan);
}
