package com.anurpeljto.gateway.services.impl;

import com.anurpeljto.gateway.domain.fiscalization.Receipt;
import com.anurpeljto.gateway.domain.loan.Loan;
import com.anurpeljto.gateway.domain.subsidy.Subsidy;
import com.anurpeljto.gateway.domain.subsidy.SubsidyGrant;
import com.anurpeljto.gateway.dto.SubsidyPageDTO;
import com.anurpeljto.gateway.services.SubsidyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
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

    public SubsidyPageDTO listSubsidies(Integer page, Integer size) {
        try{
            String requestUrl = String.format("%s/list?page=%d&size=%d", subsidyUrl, page, size);
            ResponseEntity<SubsidyPageDTO> response = restTemplate.getForEntity(requestUrl, SubsidyPageDTO.class);
            return response.getBody();
        } catch(HttpServerErrorException.InternalServerError e){
            return null;
        }
    }

    @Override
    public void publishSubsidy(Subsidy subsidy){
        try {
            OffsetDateTime now = OffsetDateTime.now();
            subsidy.setTimestamp(now);
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

    @Override
    public SubsidyPageDTO getSubsidiesByUserId(Integer id, Integer page, Integer size, String filterBy, String sortBy) {
        try{
            String reqUrl = String.format("%s/subsidy/user/%d?page=%d&size=%d&filterBy=%s&sortBy=%s", subsidyUrl, id, page, size, filterBy, sortBy);
            ResponseEntity<SubsidyPageDTO> response = restTemplate.getForEntity(reqUrl, SubsidyPageDTO.class);
            return response.getBody();
        } catch(HttpServerErrorException.InternalServerError e){
            return null;
        }
    }

    @Override
    public SubsidyPageDTO getPendingSubsidies(Integer page, Integer size) {
        try{
            String req = String.format("%s/pending?page=%d&size=%d", subsidyUrl, page, size);
            ResponseEntity<SubsidyPageDTO> response = restTemplate.getForEntity(req, SubsidyPageDTO.class);
            return response.getBody();
        }catch(HttpServerErrorException.InternalServerError e){
            return null;
        }
    }

    @Override
    public SubsidyPageDTO getWeeklySubsidies(Integer page, Integer size) {
        try{
            String req = String.format("%s/week?page=%d&size=%d", subsidyUrl, page, size);
            ResponseEntity<SubsidyPageDTO> response = restTemplate.getForEntity(req, SubsidyPageDTO.class);
            return response.getBody();
        }catch(HttpServerErrorException.InternalServerError e){
            return null;
        }
    }
}
