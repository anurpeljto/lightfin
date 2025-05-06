package com.anurpeljto.fiscalizationlistener.services.impl;

import com.anurpeljto.fiscalizationlistener.domain.Item;
import com.anurpeljto.fiscalizationlistener.domain.Receipt;
import com.anurpeljto.fiscalizationlistener.repositories.FiscalizationRepository;
import com.anurpeljto.fiscalizationlistener.services.FiscalizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FiscalizationServiceImpl implements FiscalizationService {

    private final FiscalizationRepository fiscalizationRepository;

    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public FiscalizationServiceImpl(FiscalizationRepository fiscalizationRepository, final ObjectMapper objectMapper, final KafkaTemplate<String, String> kafkaTemplate) {
        this.fiscalizationRepository = fiscalizationRepository;
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendToFiscalize(Integer id){
        // I am simulating sending and processing data (which would usually go from the physical cashier to government and then back to the user
        Receipt receipt = fiscalizationRepository.findById(id).orElseThrow(()->new RuntimeException("receipt not found"));
        receipt.setStatus("FISCALIZED");
        fiscalizationRepository.save(receipt);
    }

    @Override
    public Receipt saveToDatabase(Receipt receipt){
        log.info("Received receipt {}", receipt);
        return this.fiscalizationRepository.save(receipt);
    }

    @Override
    public List<Receipt> getReceipts(Pageable pageable){
        return this.fiscalizationRepository.findAll(pageable).getContent();
    }

    @Override
    public Receipt getReceipt(Integer id){
        return this.fiscalizationRepository.findById(id).orElse(null);
    }
}
