package com.anurpeljto.gateway.services;

import com.anurpeljto.gateway.domain.fiscalization.Receipt;
import com.anurpeljto.gateway.dto.ReceiptResponse;
import com.anurpeljto.gateway.dto.TodayResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FiscalizationService {

    void publishReceipt(Receipt receipt);

    List<Receipt> listReceipts(Integer page, Integer size);

    Receipt getReceiptById(Integer id);

    ReceiptResponse getFiscalizedThisWeek();

    ReceiptResponse getPendingThisWeek();

    ReceiptResponse getCancelledThisWeek();

    TodayResponse getTodaysTransactions();
}
