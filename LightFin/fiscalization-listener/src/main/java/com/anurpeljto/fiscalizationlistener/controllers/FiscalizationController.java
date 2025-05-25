package com.anurpeljto.fiscalizationlistener.controllers;

import com.anurpeljto.fiscalizationlistener.domain.Receipt;
import com.anurpeljto.fiscalizationlistener.dto.ReceiptResponseDTO;
import com.anurpeljto.fiscalizationlistener.dto.TodayDTOList;
import com.anurpeljto.fiscalizationlistener.services.FiscalizationService;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class FiscalizationController {

    private final FiscalizationService fiscalizationService;;

    public FiscalizationController(FiscalizationService fiscalizationService) {
        this.fiscalizationService = fiscalizationService;
    }

    @GetMapping(path = "/list")
    public List<Receipt> getReceipts(
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size
    ) {
        return this.fiscalizationService.getReceipts(
                PageRequest.of(
                        Optional.ofNullable(page).orElse(0),
                        Optional.ofNullable(size).orElse(10)
                )
        );
    }

    @GetMapping(path = "/receipt/{id}")
    public Receipt getReceipt(
            @PathVariable final Integer id
    ){
        return this.fiscalizationService.getReceipt(id);
    }

    @GetMapping(path = "/fiscalized/week")
    public ReceiptResponseDTO getFiscalizedWeekReceipts(){
        return this.fiscalizationService.fiscalizedReceiptsThisWeek();
    }

    @GetMapping(path = "/pending/week")
    public ReceiptResponseDTO getPendingWeekReceipts(){
        return this.fiscalizationService.pendingReceiptsThisWeek();
    }

    @GetMapping(path = "/cancelled/week")
    public ReceiptResponseDTO getCancelledWeekReceipts(){
        return this.fiscalizationService.cancelledReceiptsThisWeek();
    }

    @GetMapping(path = "/today")
    public TodayDTOList getTodayReceipts(){
        return this.fiscalizationService.getTodaysTransactions();
    }
}
