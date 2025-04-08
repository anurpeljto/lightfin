package com.anurpeljto.gateway.services.impl;

import com.anurpeljto.gateway.domain.Item;
import com.anurpeljto.gateway.domain.Receipt;
import com.anurpeljto.gateway.domain.ReceiptInput;
import com.anurpeljto.gateway.repositories.ReceiptRepository;
import com.anurpeljto.gateway.services.FiscalizationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FiscalizationServiceImpl implements FiscalizationService {

    private final ReceiptRepository receiptRepository;

    private final KafkaTemplate kafkaTemplate;

    private final ObjectMapper objectMapper;

    public FiscalizationServiceImpl(final ReceiptRepository receiptRepository, final KafkaTemplate kafkaTemplate, final ObjectMapper objectMapper){
        this.receiptRepository = receiptRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishReceipt(Receipt receipt){
        OffsetDateTime now = OffsetDateTime.now();
        receipt.setTimestamp(now);

        for (Item item : receipt.getItems()) {
            item.setReceipt(receipt);
        }

        StringBuilder signatureInput = new StringBuilder();
        for (Item item : receipt.getItems()) {
            signatureInput.append(item.getName())
                    .append(item.getUnitPrice())
                    .append(item.getQuantity())
                    .append(item.getTotalPrice())
                    .append(item.getTaxAmount());
        }
        signatureInput.append(receipt.getTotal()).append(now.toString());

        String fiscalCode = "MOCK" + Integer.toHexString(signatureInput.hashCode()).toUpperCase();
        receipt.setFiscalCode(fiscalCode);

        receipt.setStatus("FISCALIZED");

        try {
            String receiptJson = objectMapper.writeValueAsString(receipt);
            kafkaTemplate.send("receipt.fiscalize", receiptJson);
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
