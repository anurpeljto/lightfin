package com.anurpeljto.gateway.dto.loan;

import com.anurpeljto.gateway.domain.loan.Loan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LoanResponseDto {
    private List<Loan> data;

    public LoanResponseDto(List<Loan> data) {
        this.data = data;
    }
}
