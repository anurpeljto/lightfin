package com.anurpeljto.gateway.services;

import com.anurpeljto.gateway.domain.loan.Loan;
import com.anurpeljto.gateway.dto.loan.LoanResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LoanService {

    Loan getLoanById(Integer id);

    void approveLoan(Loan loan);

    void rejectLoan(Loan loan);

    void publishLoan(Loan loan);

    LoanResponseDto getLoansByUserId(Integer userId, Integer page, Integer size, String filterBy, String sortBy);
}
