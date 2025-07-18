package com.anurpeljto.loanlistener.controllers;

import com.anurpeljto.loanlistener.domain.Loan;
import com.anurpeljto.loanlistener.dto.LoanDto;
import com.anurpeljto.loanlistener.dto.LoanResponseDto;
import com.anurpeljto.loanlistener.dto.LoanResponsePagedDTO;
import com.anurpeljto.loanlistener.exceptions.LoanNotFound;
import com.anurpeljto.loanlistener.services.LoanService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public LoanResponsePagedDTO getLoans(
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size,
            @RequestParam(required = false) final String filterBy,
            @RequestParam(required = false) final String sortBy
    ) {
        int pageNum = Optional.ofNullable(page).orElse(0);
        int pageSize = Optional.ofNullable(size).orElse(10);
        String sortField = Optional.ofNullable(filterBy).orElse("id");

        Sort.Direction direction;
        try {
            direction = Sort.Direction.valueOf(
                    Optional.ofNullable(sortBy).orElse("DESC").toUpperCase()
            );
        } catch (IllegalArgumentException e) {
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(direction, sortField));
        Page<Loan> loans = loanService.getLoans(pageable);
        return new LoanResponsePagedDTO(loans);
    }

    @GetMapping(path = "/loan/{id}")
    public Loan getLoan(
            @PathVariable final Integer id
    ){
        Loan loan = this.loanService.getLoanById(id);
        return loan;
    }

    @GetMapping(path = "/loan/user/{id}")
    public LoanResponseDto getLoansByUserId(
            @PathVariable final Integer id,
            @RequestParam(required = false, defaultValue = "0") final Integer page,
            @RequestParam(required = false, defaultValue = "20") final Integer size,
            @RequestParam(required = false) final String filterBy,
            @RequestParam(required = false) final String sortBy
    ) {
        String sortField = (filterBy == null || filterBy.isBlank()) ? "id" : filterBy;
        Sort.Direction direction;
        try {
            direction = Sort.Direction.valueOf((sortBy == null) ? "DESC" : sortBy.toUpperCase());
        } catch (IllegalArgumentException e) {
            direction = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        List<Loan> loans = this.loanService.getLoansByUserId(id, pageable).getContent();

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

    @GetMapping(path = "/loans/week")
    public Long getLoansThisWeek() {
        return loanService.getLoansThisWeek();
    }

    @GetMapping(path = "/loans/pending")
    public Long getPendingLoans(){
        return loanService.getPendingLoans();
    }

    @GetMapping(path = "/loans/average")
    public Double getAverageLoanAmount(){
        return loanService.getAverageLoanAmount();
    }

    @GetMapping(path = "/loans/total")
    public Long getTotalLoans(){
        return loanService.getTotalLoans();
    }
}
