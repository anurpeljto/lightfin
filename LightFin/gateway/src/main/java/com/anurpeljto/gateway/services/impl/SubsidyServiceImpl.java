package com.anurpeljto.gateway.services.impl;

import com.anurpeljto.gateway.domain.loan.Loan;
import com.anurpeljto.gateway.domain.subsidy.Subsidy;
import com.anurpeljto.gateway.domain.subsidy.SubsidyGrant;
import com.anurpeljto.gateway.repositories.subsidies.SubsidyRepository;
import com.anurpeljto.gateway.services.SubsidyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubsidyServiceImpl implements SubsidyService {

    private KafkaTemplate<String, String> kafkaTemplate;

    private SubsidyRepository subsidyRepository;

    private final ObjectMapper objectMapper;

    public SubsidyServiceImpl(KafkaTemplate<String, String> kafkaTemplate, SubsidyRepository subsidyRepository, final ObjectMapper objectMapper){
        this.kafkaTemplate = kafkaTemplate;
        this.subsidyRepository = subsidyRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Subsidy> listSubsidies(Pageable pageable){
        return subsidyRepository.findAll(pageable).getContent();
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
    public Optional<Subsidy> getSubsidyById(Integer id){
        return subsidyRepository.findById(id);
    }
}
