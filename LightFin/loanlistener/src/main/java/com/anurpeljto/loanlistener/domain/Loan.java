package com.anurpeljto.loanlistener.domain;


import com.anurpeljto.loanlistener.model.LoanStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="borrower_id")
    private Integer borrowerId;

    private Double amount;

    @Column(name="interest_rate")
    private Double interestRate;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;
}
