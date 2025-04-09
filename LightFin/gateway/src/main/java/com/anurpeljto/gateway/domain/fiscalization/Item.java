package com.anurpeljto.gateway.domain.fiscalization;


import com.anurpeljto.gateway.domain.fiscalization.Receipt;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "unit_price")
    private Double unitPrice;

    private int quantity;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "tax_amount")
    private double taxAmount;

    @ManyToOne
    @JoinColumn(name = "receipt_id")
    @JsonBackReference
    private Receipt receipt;

}
