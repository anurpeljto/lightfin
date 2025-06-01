package com.anurpeljto.loanlistener.dto;

import com.anurpeljto.loanlistener.model.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanDto {
    private Integer id;
    private Integer borrowerId;
    private Double amount;
    private Double interestRate;
    private LoanStatus status;
}
