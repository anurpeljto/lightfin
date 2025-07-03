package com.anurpeljto.gateway.domain.loan;

import com.anurpeljto.gateway.domain.user.User;
import com.anurpeljto.gateway.model.LoanStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "borrower_id")
    private Integer borrowerId;

    private Double amount;

    @Column(name = "interest_rate")
    private Double interestRate;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    private OffsetDateTime timestamp;
}
