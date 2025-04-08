package com.anurpeljto.fiscalizationlistener.listeners;


import com.anurpeljto.fiscalizationlistener.domain.Receipt;
import com.anurpeljto.fiscalizationlistener.services.FiscalizationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class FiscalizeListener {

    private final ObjectMapper objectMapper;

    private final FiscalizationService fiscalizationService;

    public FiscalizeListener(final ObjectMapper obj, final FiscalizationService fiscalizationService){
        this.objectMapper = obj;
        this.fiscalizationService = fiscalizationService;
    }

    @KafkaListener(topics = "receipt.fiscalize")
    public void listen(final String in){
        try{
            log.info("Received receipt: {}", in);
            final Receipt receipt = objectMapper.readValue(in, Receipt.class);
            log.info("Items: {}", receipt.getItems());
            fiscalizationService.saveToDatabase(receipt);
        } catch (JsonProcessingException ex){
            throw new RuntimeException(ex);
        }
    }
}
