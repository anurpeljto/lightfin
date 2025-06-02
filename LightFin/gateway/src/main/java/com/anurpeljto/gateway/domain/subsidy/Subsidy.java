package com.anurpeljto.gateway.domain.subsidy;

import com.anurpeljto.gateway.model.SubsidyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "subsidies")
public class Subsidy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne
    @JoinColumn(name = "grant_id")
    private SubsidyGrant grant;

    @Column(name = "recipient_id")
    private Integer recipientId;

    private BigDecimal amount;

    private Date approvalDate;

    private Date valid_until;

    private SubsidyStatus status;

}
