package com.anurpeljto.gateway.domain.fiscalization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptInput {

    private Double total;

    private List<ItemInput> items;

    private String fiscalCode;

    private String signature;

    private String LocalDateTime;

    private String status;
}
