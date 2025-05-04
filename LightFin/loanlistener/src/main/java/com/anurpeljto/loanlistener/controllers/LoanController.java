package com.anurpeljto.loanlistener.controllers;

import com.anurpeljto.loanlistener.domain.Loan;
import com.anurpeljto.loanlistener.services.LoanService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
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
}
