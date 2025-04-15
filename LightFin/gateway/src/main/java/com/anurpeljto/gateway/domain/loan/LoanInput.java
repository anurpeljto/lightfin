package com.anurpeljto.gateway.domain.loan;

import lombok.Data;

@Data
public class LoanInput {
    private Integer borrowerId;
    private Double amount;
    private Double interestRate;
}
