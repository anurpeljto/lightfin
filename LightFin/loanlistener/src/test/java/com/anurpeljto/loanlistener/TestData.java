package com.anurpeljto.loanlistener;

import com.anurpeljto.loanlistener.domain.Loan;
import com.anurpeljto.loanlistener.model.LoanStatus;


public class TestData {

    public static Loan getTestLoan(){
        return Loan.builder()
                .id(Integer.valueOf(1))
                .amount(Double.valueOf(10.00))
                .status(LoanStatus.PENDING)
                .interestRate(Double.valueOf(0.10))
                .borrowerId(Integer.valueOf(1))
                .build();
    }
}
