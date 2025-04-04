package com.anurpeljto.gateway.services.impl;

import com.anurpeljto.gateway.domain.Loan;
import com.anurpeljto.gateway.repositories.LoanRepository;
import com.anurpeljto.gateway.services.LoanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LoanServiceImpl implements LoanService {

    private LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository){
        this.loanRepository = loanRepository;
    }

    @Override
    public List<Loan> getLoans(final Pageable pageable){
        log.info("Paging loans...");
        return loanRepository.findAll();
    }

    @Override
    public Optional<Loan> getLoanById(Integer id){
        log.info("Querying for loan with id {}", id);
        return loanRepository.findById(id);
    }
}
