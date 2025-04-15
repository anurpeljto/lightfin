package com.anurpeljto.loanlistener.services;

import com.anurpeljto.loanlistener.domain.Loan;

public interface LoanService {

    Loan saveLoan(Loan loan);

    void deleteLoan(Loan loan);

    Loan updateLoan(Loan loan);

    void approveLoan(Loan loan);

    void rejectLoan(Loan loan);
}
