package com.anurpeljto.loanlistener.dto;

import com.anurpeljto.loanlistener.domain.Loan;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LoanResponseDto {
    private List<LoanDto> data;

    public LoanResponseDto(List<LoanDto> data) {
        this.data = data;
    }
}
