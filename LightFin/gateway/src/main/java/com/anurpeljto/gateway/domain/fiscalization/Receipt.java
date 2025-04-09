package com.anurpeljto.gateway.domain.fiscalization;

import com.anurpeljto.gateway.model.PaymentType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
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
    private Integer id;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Item> items;

    private double total;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    @Column(name = "fiscal_code")
    private String fiscalCode;

    private String signature;

    private OffsetDateTime timestamp;

    private String status;
}
