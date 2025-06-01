package com.anurpeljto.loanlistener.controllers;

import com.anurpeljto.loanlistener.domain.Loan;
import com.anurpeljto.loanlistener.dto.LoanDto;
import com.anurpeljto.loanlistener.dto.LoanResponseDto;
import com.anurpeljto.loanlistener.exceptions.LoanNotFound;
import com.anurpeljto.loanlistener.services.LoanService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
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

    @GetMapping(path = "/user/{id}/report", produces = "application/pdf")
    public ResponseEntity<byte[]> getLoanReport(
            @PathVariable final Integer id,
            @RequestParam final String first_name,
            @RequestParam final String last_name,
            @RequestParam final String email
            ) {
        byte[] pdfFile =  loanService.generateLoanReport(id, first_name, last_name, email);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("loan_report_user_" + first_name + "_" + last_name + ".pdf")
                .build());

        return new ResponseEntity<>(pdfFile, headers, HttpStatus.OK);
    }
}
