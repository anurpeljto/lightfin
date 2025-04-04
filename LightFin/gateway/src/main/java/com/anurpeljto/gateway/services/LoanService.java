package com.anurpeljto.gateway.services;

import com.anurpeljto.gateway.domain.Loan;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LoanService {

    List<Loan> getLoans(Pageable pageable);

    Optional<Loan> getLoanById(Integer id);
}
