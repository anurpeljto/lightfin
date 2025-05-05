package com.anurpeljto.loanlistener.services;

import com.anurpeljto.loanlistener.domain.Loan;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LoanService {

    Loan saveLoan(Loan loan);

    void deleteLoan(Loan loan);

    Loan updateLoan(Loan loan);

    void approveLoan(Loan loan);

    void rejectLoan(Loan loan);

    List<Loan> getLoans(Pageable pageable);

    Loan getLoanById(Integer id);
}
