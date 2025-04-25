package com.anurpeljto.gateway.domain.subsidy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubsidyInput {

    private SubsidyGrant grant;

    private Integer recipientId;

    private Float amount;

}
