package com.anurpeljto.fiscalizationlistener.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
@ToString(exclude = "receipt")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Double unitPrice;

    private int quantity;

    private double totalPrice;

    @ManyToOne
    @JoinColumn(name = "receipt_id")
    @JsonBackReference
    private Receipt receipt;

}
