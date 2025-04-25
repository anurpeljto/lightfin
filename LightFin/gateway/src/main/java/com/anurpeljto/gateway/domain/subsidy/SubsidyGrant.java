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
@Table(name = "subsidy_grants")
public class SubsidyGrant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "granting_authority")
    private String grantingAuthority;

    private String description;
}
