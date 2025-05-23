package com.anurpeljto.gateway.services.impl;

import com.anurpeljto.gateway.domain.fiscalization.Receipt;
import com.anurpeljto.gateway.domain.loan.Loan;
import com.anurpeljto.gateway.domain.subsidy.Subsidy;
import com.anurpeljto.gateway.domain.subsidy.SubsidyGrant;
import com.anurpeljto.gateway.services.SubsidyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class SubsidyServiceImpl implements SubsidyService {

    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    private RestTemplate restTemplate;

    @Value("${spring.subsidy.url}")
    private String subsidyUrl;

    public SubsidyServiceImpl(KafkaTemplate<String, String> kafkaTemplate, final ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    public List<Subsidy> listSubsidies(Integer page, Integer size) {
        try{
            String requestUrl = String.format("%s/list?page=%d&size=%d", subsidyUrl, page, size);
            ResponseEntity<List> response = restTemplate.getForEntity(requestUrl, List.class);
            return response.getBody();
        } catch(HttpServerErrorException.InternalServerError e){
            return null;
        }
    }

    @Override
    public void publishSubsidy(Subsidy subsidy){
        try {
            String payload = objectMapper.writeValueAsString(subsidy);
            kafkaTemplate.send("subsidy.published", payload);
        } catch (JsonProcessingException ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void approveSubsidy(Integer id){
        kafkaTemplate.send("subsidy.approved", id.toString());
    }

    @Override
    public void rejectSubsidy(Integer id){
        kafkaTemplate.send("subsidy.rejected", id.toString());
    }

    @Override
    public void deleteSubsidy(Integer id){
        kafkaTemplate.send("subsidy.deleted", id.toString());
    }

    @Override
    public Subsidy getSubsidyById(Integer id){
        try{
            String requestUrl = String.format("%s/subsidy/%s", subsidyUrl, id);
            ResponseEntity<Subsidy> response = restTemplate.getForEntity(requestUrl, Subsidy.class);
            return response.getBody();
        } catch(HttpServerErrorException.InternalServerError e){
            return null;
        }
    }
}
