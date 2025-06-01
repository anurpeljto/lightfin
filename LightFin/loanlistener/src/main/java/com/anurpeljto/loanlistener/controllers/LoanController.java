package com.anurpeljto.loanlistener.controllers;

import com.anurpeljto.loanlistener.domain.Loan;
import com.anurpeljto.loanlistener.dto.LoanDto;
import com.anurpeljto.loanlistener.dto.LoanResponseDto;
import com.anurpeljto.loanlistener.exceptions.LoanNotFound;
import com.anurpeljto.loanlistener.services.LoanService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping(path = "/list")
    public List<Loan> getLoans(
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size
    ) {
        return this.loanService.getLoans(
                PageRequest.of(
                        Optional.ofNullable(page).orElse(0),
                        Optional.ofNullable(size).orElse(10)
                )
        );
    }

    @GetMapping(path = "/loan/{id}")
    public Loan getLoan(
            @PathVariable final Integer id
    ){
        Loan loan = this.loanService.getLoanById(id);
        return loan;
    }

    @GetMapping(path = "/loan/user/{id}")
    public LoanResponseDto getLoans(
            @PathVariable final Integer id,
            @RequestParam(required = false, defaultValue = "0") final Integer page,
            @RequestParam(required = false, defaultValue = "20") final Integer size
    ){
        List<Loan> loans = this.loanService.getLoansByUserId(id, PageRequest.of(page, size)).getContent();
        List<LoanDto> dtos = loans.stream()
                .map(l -> new LoanDto(l.getId(), l.getBorrowerId(), l.getAmount(), l.getInterestRate(), l.getStatus()))
                .toList();
        return new LoanResponseDto(dtos);
    }
}
