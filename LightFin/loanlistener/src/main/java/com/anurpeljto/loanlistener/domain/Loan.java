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

    @ManyToOne
    @JoinColumn(name="borrower_id")
    private User borrower;

    private Double amount;

    private Double interest_rate;

    private LoanStatus status;
}
