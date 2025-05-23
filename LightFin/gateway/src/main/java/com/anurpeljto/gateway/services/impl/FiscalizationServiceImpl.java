package com.anurpeljto.gateway.services.impl;

import com.anurpeljto.gateway.domain.fiscalization.Item;
import com.anurpeljto.gateway.domain.fiscalization.Receipt;
import com.anurpeljto.gateway.domain.loan.Loan;
import com.anurpeljto.gateway.dto.ReceiptResponse;
import com.anurpeljto.gateway.model.FiscalizationStatus;
import com.anurpeljto.gateway.services.FiscalizationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FiscalizationServiceImpl implements FiscalizationService {

    private final KafkaTemplate kafkaTemplate;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    @Value("${spring.fiscalization.url}")
    private String fiscalizationUrl;

    public FiscalizationServiceImpl(final KafkaTemplate kafkaTemplate, final ObjectMapper objectMapper, final RestTemplate restTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public void publishReceipt(Receipt receipt){
        OffsetDateTime now = OffsetDateTime.now();
        receipt.setTimestamp(now);

        if(receipt.getStatus() == null){
            receipt.setStatus(FiscalizationStatus.PENDING.toString());
        }

        for (Item item : receipt.getItems()) {
            item.setReceipt(receipt);
        }

        StringBuilder signatureInput = new StringBuilder();
        double total = 0.00;
        for (Item item : receipt.getItems()) {
            item.setTotalPrice(item.getQuantity() * item.getUnitPrice());
            total+=item.getTotalPrice();

            signatureInput.append(item.getName())
                    .append(item.getUnitPrice())
                    .append(item.getQuantity());
        }
        signatureInput.append(receipt.getTotal()).append(now.toString());
        receipt.setTotal(total + (total * receipt.getTaxAmount()));

        String fiscalCode = "MOCK" + Integer.toHexString(signatureInput.hashCode()).toUpperCase();
        receipt.setFiscalCode(fiscalCode);
        
        try {
            String receiptJson = objectMapper.writeValueAsString(receipt);
            kafkaTemplate.send("receipt.publish", receiptJson);
        } catch (JsonProcessingException e) {
            log.error("Error serializing receipt: ", e);
        }
    }

    @Override
    public List<Receipt> listReceipts(Integer page, Integer size) {
        try{
            String requestUrl = String.format("%s/list?page=%d&size=%d", fiscalizationUrl, page, size);
            ResponseEntity<List> response = restTemplate.getForEntity(requestUrl, List.class);
            return response.getBody();
        } catch(HttpServerErrorException.InternalServerError e){
            return null;
        }
    }

    @Override
    public Receipt getReceiptById(Integer id) {
        try{
            String requestUrl = String.format("%s/receipt/%s", fiscalizationUrl, id);
            ResponseEntity<Receipt> response = restTemplate.getForEntity(requestUrl, Receipt.class);
            return response.getBody();
        } catch(HttpServerErrorException.InternalServerError e){
            return null;
        }
    }

    @Override
    public ReceiptResponse getFiscalizedThisWeek() {
        try{
            String requestUrl = String.format("%s/fiscalized/week", fiscalizationUrl);
            log.info("Fiscalized this week");
            ResponseEntity<ReceiptResponse> response = restTemplate.getForEntity(requestUrl, ReceiptResponse.class);
            return response.getBody();
        } catch(HttpServerErrorException.InternalServerError e){
            return null;
        }
    }
}
