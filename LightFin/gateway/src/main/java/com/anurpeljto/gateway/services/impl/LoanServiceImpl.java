package com.anurpeljto.gateway.services.impl;

import com.anurpeljto.gateway.domain.loan.Loan;
import com.anurpeljto.gateway.services.LoanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LoanServiceImpl implements LoanService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${spring.loans.url}")
    private String loanServiceUrl;

    public LoanServiceImpl(KafkaTemplate<String, String> kafkaTemplate, final ObjectMapper objectMapper, final RestTemplate restTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<Loan> getLoanById(Integer id){
        ResponseEntity<Loan> response = restTemplate.getForEntity(loanServiceUrl + "/loan/" + id, Loan.class);
        return Optional.ofNullable(response.getBody());
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
