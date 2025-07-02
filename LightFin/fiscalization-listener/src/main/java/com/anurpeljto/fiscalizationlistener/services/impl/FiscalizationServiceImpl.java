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
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
    public TodayDTOList getTodaysTransactions() {
        ZoneOffset offset = OffsetDateTime.now().getOffset();

        OffsetDateTime now = OffsetDateTime.now();
        LocalDate today = now.toLocalDate();

        OffsetDateTime startOfDay = today.atStartOfDay().atOffset(offset);
        OffsetDateTime endOfDay = today.plusDays(1).atStartOfDay().atOffset(offset);

        List<TodayDTO> receipts = this.fiscalizationRepository.todaysTransactions(startOfDay, endOfDay);
        log.info("Total receipts {}", receipts.size());
        return new TodayDTOList(receipts);
    }

    @Override
    public WeeklyByTypeDTO getWeeklyByType() {
        OffsetDateTime today = OffsetDateTime.now();
        OffsetDateTime startOfWeek = today.minusDays(7);

        List<WeeklyByType> weeklyData = this.fiscalizationRepository.weeklyByType(startOfWeek, today);
        return new WeeklyByTypeDTO(weeklyData);
    }

    @Override
    public Integer todaysTransactionsCount() {
        ZoneOffset offset = OffsetDateTime.now().getOffset();

        OffsetDateTime now = OffsetDateTime.now();
        LocalDate today = now.toLocalDate();

        OffsetDateTime startOfDay = today.atStartOfDay().atOffset(offset);
        OffsetDateTime endOfDay = today.plusDays(1).atStartOfDay().atOffset(offset);

        Integer count = this.fiscalizationRepository.todaysTransactionsCount(startOfDay, endOfDay);
        return count;
    }

    @Override
    public Integer weeklyTransactionsCount() {
        OffsetDateTime today = OffsetDateTime.now();
        OffsetDateTime startOfWeek = today.minusDays(7);

        Integer count = this.fiscalizationRepository.weeklyTransactionsCount(startOfWeek, today);
        return count;
    }

    @Override
    public Integer monthlyTransactionsCount() {
        ZoneOffset offset = OffsetDateTime.now().getOffset();
        OffsetDateTime now = OffsetDateTime.now();

        LocalDate today = now.toLocalDate();

        LocalDate startOfMonthDate = today.withDayOfMonth(1);
        OffsetDateTime startOfMonth = startOfMonthDate.atStartOfDay().atOffset(offset);

        LocalDate startOfNextMonthDate = today.plusMonths(1).withDayOfMonth(1);
        OffsetDateTime endOfMonth = startOfNextMonthDate.atStartOfDay().atOffset(offset);

        Integer count = this.fiscalizationRepository.monthlyTransactionsCount(startOfMonth, endOfMonth);
        return count;
    }

    @Override
    public byte[] generateReceipt(Integer id){
        Optional<Receipt> receiptOptional = fiscalizationRepository.findById(id);
        if(receiptOptional.isEmpty()){
            throw new RuntimeException("Receipt id invalid");
        }
        Receipt receipt = receiptOptional.get();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        document.add(new Paragraph("Receipt"));
        Paragraph issuerParagraph = new Paragraph("Issued by: " + receipt.getSignature());
        document.add(issuerParagraph);

        LocalDateTime localDateTime = receipt.getTimestamp().toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        Paragraph timestamp = new Paragraph("Time of issuing: " + localDateTime.format(formatter));
        document.add(timestamp);

        Paragraph tax = new Paragraph("Tax: " + receipt.getTaxAmount());
        document.add(tax);

        Paragraph total = new Paragraph("Total: " + receipt.getTotal());
        document.add(total);

        Paragraph fiscalCode = new Paragraph("Fiscal Code: " + receipt.getFiscalCode());
        fiscalCode.setSpacingAfter(10f);
        document.add(fiscalCode);

        PdfPTable table = new PdfPTable(5);
        table.addCell("Item Id");
        table.addCell("Name");
        table.addCell("Unit price");
        table.addCell("Quantity");
        table.addCell("Total before tax");

        for (Item item : receipt.getItems()) {
            table.addCell(String.valueOf(item.getId()));
            table.addCell(String.valueOf(item.getName()));
            table.addCell(String.valueOf(item.getUnitPrice()));
            table.addCell(String.valueOf(item.getQuantity()));
            table.addCell(String.valueOf((item.getQuantity())*item.getUnitPrice()));
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }

    @Override
    public Float averageReceiptsPerDay() {
        return fiscalizationRepository.averageTransactionsPerDay();
    }
}
