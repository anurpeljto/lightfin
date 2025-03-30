package com.anurpeljto.loanlistener.services.impl;

import com.anurpeljto.loanlistener.domain.Loan;
import com.anurpeljto.loanlistener.repositories.LoanRepository;
import com.anurpeljto.loanlistener.services.LoanService;
import org.springframework.stereotype.Service;

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
        existingLoan.setBorrower(loan.getBorrower());
        existingLoan.setInterest_rate(loan.getInterest_rate());

        return loanRepository.save(existingLoan);
    }
}
