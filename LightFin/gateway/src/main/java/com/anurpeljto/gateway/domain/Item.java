package com.anurpeljto.gateway.domain;


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

    private Double unitPrice;

    private int quantity;

    private double totalPrice;

    private double taxAmount;

    @ManyToOne
    @JoinColumn(name = "receipt_id")
    @JsonBackReference
    private Receipt receipt;

}
