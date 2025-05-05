package com.anurpeljto.loanlistener.services.impl;

import com.anurpeljto.loanlistener.domain.Loan;
import com.anurpeljto.loanlistener.exceptions.LoanNotFound;
import com.anurpeljto.loanlistener.model.LoanStatus;
import com.anurpeljto.loanlistener.repositories.LoanRepository;
import com.anurpeljto.loanlistener.services.LoanService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {

    private LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository){
        this.loanRepository = loanRepository;
    }

    @Override
    public Loan saveLoan(Loan loan){
        return this.loanRepository.save(loan);
    }

    @Override
    public void deleteLoan(Loan loan){
        loanRepository.delete(loan);
    }

    @Override
    public Loan updateLoan(Loan loan){
        final Optional<Loan> existingLoanOpt = loanRepository.findById(loan.getId());

        if(existingLoanOpt.isEmpty()){
            return null;
        }

        Loan existingLoan = existingLoanOpt.get();

        existingLoan.setAmount(loan.getAmount());
        existingLoan.setStatus(loan.getStatus());
        existingLoan.setBorrowerId(loan.getBorrowerId());
        existingLoan.setInterestRate(loan.getInterestRate());

        return loanRepository.save(existingLoan);
    }

    @Override
    public void approveLoan(Loan loan) {
        final Optional<Loan> existingLoanOpt = loanRepository.findById(loan.getId());
        if(existingLoanOpt.isEmpty()){
            return;
        }
        Loan existingLoan = existingLoanOpt.get();
        existingLoan.setStatus(LoanStatus.APPROVED);
        loanRepository.save(existingLoan);
    }

    @Override
    public void rejectLoan(Loan loan) {
        final Optional<Loan> existingLoanOpt = loanRepository.findById(loan.getId());
        if(existingLoanOpt.isEmpty()){
            return;
        }
        Loan existingLoan = existingLoanOpt.get();
        existingLoan.setStatus(LoanStatus.REJECTED);
        loanRepository.save(existingLoan);
    }

    @Override
    public List<Loan> getLoans(Pageable pageable) {
        return this.loanRepository.findAll(pageable).getContent();
    }

    @Override
    public Loan getLoanById(Integer id){
        return loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFound("Loan not found with ID: " + id));
    }
}
