package com.anurpeljto.fiscalizationlistener.listeners;


import com.anurpeljto.fiscalizationlistener.domain.Receipt;
import com.anurpeljto.fiscalizationlistener.services.FiscalizationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
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

    @KafkaListener(topics = "receipt.publish")
    public void listen(@Payload final String in){
        try{
            log.info("Received receipt: {}", in);
            final Receipt receipt = objectMapper.readValue(in, Receipt.class);
                Receipt savedReceipt = fiscalizationService.saveToDatabase(receipt);
                fiscalizationService.sendToFiscalize(savedReceipt.getId());
            }
        catch (JsonProcessingException ex) {
            log.error("Failed to deserialize message: {}", in, ex);
            throw new RuntimeException("Failed to process message", ex);
        } catch (Exception ex) {
            log.error("Unexpected error while processing message: {}", in, ex);
            throw new RuntimeException("Unexpected error", ex);
        }
    }
}
