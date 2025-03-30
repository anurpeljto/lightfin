package com.anurpeljto.loanlistener.services;

import com.anurpeljto.loanlistener.domain.Loan;

public interface LoanService {

    Loan saveLoan(Loan loan);

    void deleteLoan(Loan loan);

    Loan updateLoan(Loan loan);
}
