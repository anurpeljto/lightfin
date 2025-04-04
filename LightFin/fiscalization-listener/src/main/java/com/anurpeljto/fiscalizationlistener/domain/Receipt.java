package com.anurpeljto.fiscalizationlistener.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "receipts")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @OneToMany
    @JoinColumn(name = "receipt_id")
    private List<Item> items;

    private double total;

    private String paymentType;

    private String fiscalCode;

    private String signature;

    private LocalDateTime timestamp;

    private String status;
}
