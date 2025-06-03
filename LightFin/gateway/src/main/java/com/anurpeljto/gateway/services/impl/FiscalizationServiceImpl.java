package com.anurpeljto.gateway.services.impl;

import com.anurpeljto.gateway.domain.fiscalization.Item;
import com.anurpeljto.gateway.domain.fiscalization.Receipt;
import com.anurpeljto.gateway.domain.loan.Loan;
import com.anurpeljto.gateway.dto.ReceiptResponse;
import com.anurpeljto.gateway.dto.TodayResponse;
import com.anurpeljto.gateway.dto.WeeklyByTypeDTO;
import com.anurpeljto.gateway.dto.WeeklyByTypeResponse;
import com.anurpeljto.gateway.dto.loan.LoanResponseDto;
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
import java.util.Arrays;
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
    public ReceiptResponse listReceipts(Integer page, Integer size, String filterBy, String sortBy) {
        try{
            String requestUrl = String.format("%s/list?page=%d&size=%d&filterBy=%s&sortBy=%s", fiscalizationUrl, page, size, filterBy, sortBy);
            ResponseEntity<ReceiptResponse> response = restTemplate.getForEntity(requestUrl, ReceiptResponse.class);
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
    public ReceiptResponse getFiscalizedThisWeek(Integer page, Integer size, String filterBy, String sortBy) {
        try{
            String requestUrl = String.format("%s/fiscalized/week?page=%d&size=%d&filterBy=%s&sortBy=%s", fiscalizationUrl, page, size, filterBy, sortBy);
            log.info("Fiscalized this week");
            ResponseEntity<ReceiptResponse> response = restTemplate.getForEntity(requestUrl, ReceiptResponse.class);
            return response.getBody();
        } catch(HttpServerErrorException.InternalServerError e){
            return null;
        }
    }

    @Override
    public ReceiptResponse getPendingThisWeek(Integer page, Integer size, String filterBy, String sortBy) {
        try{
            String requestUrl = String.format("%s/pending/week?page=%d&size=%d&filterBy=%s&sortBy=%s", fiscalizationUrl, page, size, filterBy, sortBy);
            ResponseEntity<ReceiptResponse> response = restTemplate.getForEntity(requestUrl, ReceiptResponse.class);
            return response.getBody();
        } catch(HttpServerErrorException.InternalServerError e){
            return null;
        }
    }

    @Override
    public ReceiptResponse getCancelledThisWeek(Integer page, Integer size, String filterBy, String sortBy) {
        try{
            String requestUrl = String.format("%s/cancelled/week?page=%d&size=%d&filterBy=%s&sortBy=%s", fiscalizationUrl, page, size, filterBy, sortBy);
            ResponseEntity<ReceiptResponse> response = restTemplate.getForEntity(requestUrl, ReceiptResponse.class);
            return response.getBody();
        } catch(HttpServerErrorException.InternalServerError e){
            return null;
        }
    }

    @Override
    public TodayResponse getTodaysTransactions(Integer limit) {
        try{
            String requestUrl = String.format("%s/today?limit=%d", fiscalizationUrl, limit);
            ResponseEntity<TodayResponse> response = restTemplate.getForEntity(requestUrl, TodayResponse.class);
            String value = objectMapper.writeValueAsString(response.getBody());
            log.info("Today transactions: {}", value);
            return response.getBody();
        } catch (HttpServerErrorException.InternalServerError e) {
            return null;
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public WeeklyByTypeDTO getWeeklyByType() {
        String requestUrl = String.format("%s/type/week", fiscalizationUrl);
        ResponseEntity<WeeklyByTypeDTO> response = restTemplate.getForEntity(requestUrl, WeeklyByTypeDTO.class);
        return response.getBody();
    }
}
