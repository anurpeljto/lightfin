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

    @GetMapping(path = "/today")
    public TodayDTOList getTodayReceipts(
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size,
            @RequestParam(required = false) final String filterBy,
            @RequestParam(required = false) final String sortBy
    ){
        Pageable pageable = PageableCreator.createPageable(page, size, filterBy, sortBy);
        TodayDTOList receipts = fiscalizationService.getTodaysTransactions(pageable);
        return receipts;
    }

    @GetMapping(path = "/type/week")
    public WeeklyByTypeDTO getWeeklyReceipts(){
        return this.fiscalizationService.getWeeklyByType();
    }
}
