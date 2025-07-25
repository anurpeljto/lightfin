package com.anurpeljto.loanlistener.services;

import com.anurpeljto.loanlistener.domain.Loan;
import com.anurpeljto.loanlistener.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LoanService {

    Loan saveLoan(Loan loan);

    void deleteLoan(Loan loan);

    Loan updateLoan(Loan loan);

    void approveLoan(Loan loan);

    void rejectLoan(Loan loan);

    Page<Loan> getLoans(Pageable pageable);

    Loan getLoanById(Integer id);

    Page<Loan> getLoansByUserId(Integer userId, Pageable pageable);

    byte[] generateLoanReport(Integer userId, String first_name, String last_name, String email);

    Long getTotalLoans();

    Long getPendingLoans();

    Long getLoansThisWeek();

    Double getAverageLoanAmount();
}
