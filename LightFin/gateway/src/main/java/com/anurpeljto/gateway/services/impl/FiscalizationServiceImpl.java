package com.anurpeljto.gateway.services.impl;

import com.anurpeljto.gateway.domain.fiscalization.Item;
import com.anurpeljto.gateway.domain.fiscalization.Receipt;
import com.anurpeljto.gateway.model.FiscalizationStatus;
import com.anurpeljto.gateway.repositories.receipt.ReceiptRepository;
import com.anurpeljto.gateway.services.FiscalizationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Slf4j
public class FiscalizationServiceImpl implements FiscalizationService {

    private final ReceiptRepository receiptRepository;

    private final KafkaTemplate kafkaTemplate;

    private final ObjectMapper objectMapper;

    public FiscalizationServiceImpl(@Qualifier("receiptRepository") final ReceiptRepository receiptRepository, final KafkaTemplate kafkaTemplate, final ObjectMapper objectMapper){
        this.receiptRepository = receiptRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
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
        for (Item item : receipt.getItems()) {
            signatureInput.append(item.getName())
                    .append(item.getUnitPrice())
                    .append(item.getQuantity())
                    .append(item.getTotalPrice());
        }
        signatureInput.append(receipt.getTotal()).append(now.toString());

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
    public List<Receipt> listReceipts(Pageable pageable){
        log.info("Paging loans...");
        return receiptRepository.findAll(pageable).getContent();
    }
}
