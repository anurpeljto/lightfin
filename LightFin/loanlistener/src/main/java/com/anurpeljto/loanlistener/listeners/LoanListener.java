package com.anurpeljto.loanlistener.listeners;

import com.anurpeljto.loanlistener.domain.Loan;
import com.anurpeljto.loanlistener.services.LoanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LoanListener {

    private final ObjectMapper objectMapper;

    private final LoanService loanService;

    public LoanListener(final ObjectMapper obj, final LoanService loanSrv){
        this.objectMapper = obj;
        this.loanService = loanSrv;
    }

    @KafkaListener(topics = "loan.published")
    public String listens(final String in){
        try {
            final Loan publishedLoan = objectMapper.readValue(in, Loan.class);
            loanService.saveLoan(publishedLoan);
        } catch (JsonProcessingException ex){
            throw new RuntimeException(ex);
        }
        return in;
    }
}
