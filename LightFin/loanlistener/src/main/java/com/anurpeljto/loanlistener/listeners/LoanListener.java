package com.anurpeljto.loanlistener.listeners;

import com.anurpeljto.loanlistener.domain.Loan;
import com.anurpeljto.loanlistener.services.LoanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
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
            log.info("Received a loan event: " + in);
            final Loan publishedLoan = objectMapper.readValue(in, Loan.class);
            log.info("Received a loan event: " + publishedLoan);
            loanService.saveLoan(publishedLoan);
        } catch (JsonProcessingException ex){
            throw new RuntimeException(ex);
        }
        return in;
    }

    @KafkaListener(topics = "loan.approve")
    public String listensToApprove(final String in){
        try{
            final Loan loan = objectMapper.readValue(in, Loan.class);
            loanService.approveLoan(loan);
        } catch (JsonProcessingException ex){
            throw new RuntimeException(ex);
        }
        return in;
    }

    @KafkaListener(topics = "loan.reject")
    public String listensToReject(final String in){
        try{
            final Loan loan = objectMapper.readValue(in, Loan.class);
            loanService.rejectLoan(loan);
        } catch (JsonProcessingException ex){
            throw new RuntimeException(ex);
        }
        return in;
    }
}
