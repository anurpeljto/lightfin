package com.anurpeljto.loanlistener;

import com.anurpeljto.loanlistener.domain.Loan;
import com.anurpeljto.loanlistener.domain.User;
import com.anurpeljto.loanlistener.model.LoanStatus;


public class TestData {

    public static Loan getTestLoan(){
        final User newUser = User.builder()
                .id(1)
                .email("test@gmail.com")
                .first_name("test")
                .last_name("lastname")
                .email_token("123456779")
                .emailVerified(false)
                .password("non-encrypted test")
                .passwordToken("12345678910")
                .build();
        return Loan.builder()
                .id(1)
                .amount(10.00)
                .status(LoanStatus.PENDING)
                .interest_rate(0.10)
                .borrower(newUser)
                .build();
    }
}
