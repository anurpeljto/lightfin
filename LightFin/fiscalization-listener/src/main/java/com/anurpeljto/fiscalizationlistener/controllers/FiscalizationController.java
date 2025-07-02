package com.anurpeljto.fiscalizationlistener.controllers;

import com.anurpeljto.fiscalizationlistener.domain.Receipt;
import com.anurpeljto.fiscalizationlistener.domain.WeeklyByType;
import com.anurpeljto.fiscalizationlistener.dto.ReceiptResponseDTO;
import com.anurpeljto.fiscalizationlistener.dto.TodayDTO;
import com.anurpeljto.fiscalizationlistener.dto.TodayDTOList;
import com.anurpeljto.fiscalizationlistener.dto.WeeklyByTypeDTO;
import com.anurpeljto.fiscalizationlistener.services.FiscalizationService;
import com.anurpeljto.fiscalizationlistener.utils.PageableCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class FiscalizationController {

    private final FiscalizationService fiscalizationService;;

    public FiscalizationController(FiscalizationService fiscalizationService) {
        this.fiscalizationService = fiscalizationService;
    }

    @GetMapping(path = "/list")
    public ReceiptResponseDTO getReceipts(
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size,
            @RequestParam(required = false) final String filterBy,
            @RequestParam(required = false) final String sortBy
    ) {
        log.info("Params: {}, {}, {} ,{}", page, size, filterBy, sortBy);
        Pageable pageable = PageableCreator.createPageable(page, size, filterBy, sortBy);
        log.info("pageable: {}", pageable);
        Page<Receipt> receipts = fiscalizationService.getReceipts(pageable);
        return new ReceiptResponseDTO(receipts);
    }

    @GetMapping(path = "/receipt/{id}")
    public Receipt getReceipt(
            @PathVariable final Integer id
    ){
        return this.fiscalizationService.getReceipt(id);
    }

    @GetMapping(path = "/fiscalized/week")
    public ReceiptResponseDTO getFiscalizedWeekReceipts(
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size,
            @RequestParam(required = false) final String filterBy,
            @RequestParam(required = false) final String sortBy
    ){
        Pageable pageable = PageableCreator.createPageable(page, size, filterBy, sortBy);
        Page<Receipt> receipts = fiscalizationService.fiscalizedReceiptsThisWeek(pageable);
        return new ReceiptResponseDTO(receipts);
    }

    @GetMapping(path = "/pending/week")
    public ReceiptResponseDTO getPendingWeekReceipts(
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size,
            @RequestParam(required = false) final String filterBy,
            @RequestParam(required = false) final String sortBy
    ){
        Pageable pageable = PageableCreator.createPageable(page, size, filterBy, sortBy);
        Page<Receipt> receipts = fiscalizationService.pendingReceiptsThisWeek(pageable);
        return new ReceiptResponseDTO(receipts);
    }

    @GetMapping(path = "/cancelled/week")
    public ReceiptResponseDTO getCancelledWeekReceipts(
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size,
            @RequestParam(required = false) final String filterBy,
            @RequestParam(required = false) final String sortBy
    ){
        Pageable pageable = PageableCreator.createPageable(page, size, filterBy, sortBy);
        Page<Receipt> receipts = fiscalizationService.cancelledReceiptsThisWeek(pageable);
        return new ReceiptResponseDTO(receipts);
    }

    @GetMapping(path = "/receipt/{id}/generate", produces = "application/pdf")
    public ResponseEntity<byte[]> generateReceipt(
            @PathVariable final Integer id
    ){
        byte[] pdf = fiscalizationService.generateReceipt(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("receipt_" + id.toString() + ".pdf")
                .build()
        );

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @GetMapping(path = "/today")
    public TodayDTOList getTodayReceipts(){
        TodayDTOList receipts = fiscalizationService.getTodaysTransactions();
//        TodayDTOList receiptList = new TodayDTOList(receipts);
        return receipts;
    }

    @GetMapping(path = "/type/week")
    public WeeklyByTypeDTO getWeeklyReceipts(){
        return this.fiscalizationService.getWeeklyByType();
    }

    @GetMapping(path = "/today/count")
    public Integer getTodaysTransactionsCount() {
        return this.fiscalizationService.todaysTransactionsCount();
    }

    @GetMapping(path = "/week/count")
    public Integer weeklyTransactionsCount() {
        return this.fiscalizationService.weeklyTransactionsCount();
    }

    @GetMapping(path = "/month/count")
    public Integer monthlyTransactionsCount() {
        return this.fiscalizationService.monthlyTransactionsCount();
    }

    @GetMapping(path = "/average")
    public Float averageReceiptsPerDay(){
        return this.fiscalizationService.averageReceiptsPerDay();
    }
}
