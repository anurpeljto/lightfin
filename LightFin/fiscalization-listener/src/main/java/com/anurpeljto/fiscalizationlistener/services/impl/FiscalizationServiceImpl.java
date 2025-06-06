package com.anurpeljto.fiscalizationlistener.services.impl;

import com.anurpeljto.fiscalizationlistener.domain.Item;
import com.anurpeljto.fiscalizationlistener.domain.Receipt;
import com.anurpeljto.fiscalizationlistener.domain.WeeklyByType;
import com.anurpeljto.fiscalizationlistener.dto.ReceiptResponseDTO;
import com.anurpeljto.fiscalizationlistener.dto.TodayDTO;
import com.anurpeljto.fiscalizationlistener.dto.TodayDTOList;
import com.anurpeljto.fiscalizationlistener.dto.WeeklyByTypeDTO;
import com.anurpeljto.fiscalizationlistener.repositories.FiscalizationRepository;
import com.anurpeljto.fiscalizationlistener.services.FiscalizationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
    public Page<Receipt> getReceipts(Pageable pageable){
        return this.fiscalizationRepository.findAll(pageable);
    }

    @Override
    public Receipt getReceipt(Integer id){
        return this.fiscalizationRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Receipt> fiscalizedReceiptsThisWeek(Pageable pageable) {
        OffsetDateTime today = OffsetDateTime.now();
        OffsetDateTime startOfWeek = today.minusDays(7);
        Page<Receipt> receipts = this.fiscalizationRepository.findFiscalizedByThisWeek(startOfWeek, today, pageable);
        return receipts;
    }

    @Override
    public Page<Receipt> pendingReceiptsThisWeek(Pageable pageable) {
        OffsetDateTime today = OffsetDateTime.now();
        OffsetDateTime startOfWeek = today.minusDays(7);
        Page<Receipt> receipts = this.fiscalizationRepository.findPendingByThisWeek(startOfWeek, today, pageable);
        return receipts;
    }

    @Override
    public Page<Receipt> cancelledReceiptsThisWeek(Pageable pageable) {
        OffsetDateTime today = OffsetDateTime.now();
        OffsetDateTime startOfWeek = today.minusDays(7);
        Page<Receipt> receipts = this.fiscalizationRepository.findCancelledByThisWeek(startOfWeek, today, pageable);
        return receipts;
    }

    @Override
    public TodayDTOList getTodaysTransactions(Pageable pageable) {
        ZoneOffset offset = OffsetDateTime.now().getOffset();

        OffsetDateTime now = OffsetDateTime.now();
        LocalDate today = now.toLocalDate();

        OffsetDateTime startOfDay = today.atStartOfDay().atOffset(offset);
        OffsetDateTime endOfDay = today.plusDays(1).atStartOfDay().atOffset(offset);

        Page<TodayDTO> receipts = this.fiscalizationRepository.todaysTransactions(startOfDay, endOfDay, pageable);
        return new TodayDTOList(receipts);
    }

    @Override
    public WeeklyByTypeDTO getWeeklyByType() {
        OffsetDateTime today = OffsetDateTime.now();
        OffsetDateTime startOfWeek = today.minusDays(7);

        List<WeeklyByType> weeklyData = this.fiscalizationRepository.weeklyByType(startOfWeek, today);
        return new WeeklyByTypeDTO(weeklyData);
    }
}
