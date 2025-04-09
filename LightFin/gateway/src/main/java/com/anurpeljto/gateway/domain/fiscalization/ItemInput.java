package com.anurpeljto.gateway.domain.fiscalization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemInput {

    private String name;

    private Double unitPrice;

    private Integer quantity;

    private Double totalPrice;

    private Double taxAmount;
}
