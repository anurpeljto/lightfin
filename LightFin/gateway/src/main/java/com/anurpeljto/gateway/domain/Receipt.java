package com.anurpeljto.gateway.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    private String paymentType;

    private String fiscalCode;

    private String signature;

    private OffsetDateTime timestamp;

    private String status;
}
