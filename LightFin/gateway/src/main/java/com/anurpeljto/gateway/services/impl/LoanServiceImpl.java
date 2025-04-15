package com.anurpeljto.gateway.services.impl;

import com.anurpeljto.gateway.domain.loan.Loan;
import com.anurpeljto.gateway.repositories.loan.LoanRepository;
import com.anurpeljto.gateway.services.LoanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public LoanServiceImpl(final LoanRepository loanRepository, KafkaTemplate<String, String> kafkaTemplate, final ObjectMapper objectMapper) {
        this.loanRepository = loanRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
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

    @Override
    public void approveLoan(Loan loan) {
        try{
            log.info("Approving loan with id {}", loan.getId());
            String payload = objectMapper.writeValueAsString(loan);
            kafkaTemplate.send("loan.approve", payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rejectLoan(Loan loan) {
        try{
            log.info("Rejecting loan with id {}", loan.getId());
            String payload = objectMapper.writeValueAsString(loan);
            kafkaTemplate.send("loan.reject", payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void publishLoan(Loan loan) {
        try{
            log.info("Publishing loan with id {}", loan.getId());
            String payload = objectMapper.writeValueAsString(loan);
            kafkaTemplate.send("loan.published", payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
