package com.anurpeljto.fiscalizationlistener.listeners;


import com.anurpeljto.fiscalizationlistener.domain.Receipt;
import com.anurpeljto.fiscalizationlistener.services.FiscalizationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class FiscalizeListener {

    private final ObjectMapper objectMapper;

    private final FiscalizationService fiscalizationService;

    public FiscalizeListener(final ObjectMapper obj, final FiscalizationService fiscalizationService){
        this.objectMapper = obj;
        this.fiscalizationService = fiscalizationService;
    }

    @KafkaListener(topics = "receipt.fiscalize")
    public String listen(final String in){
        try{
            final Receipt input = objectMapper.readValue(in, Receipt.class);
            fiscalizationService.fiscalize(input);
        } catch (JsonProcessingException ex){
            throw new RuntimeException(ex);
        }

        return in;
    }
}
