package com.anurpeljto.gateway.services;

import com.anurpeljto.gateway.domain.fiscalization.Receipt;
import com.anurpeljto.gateway.dto.ReceiptResponse;
import com.anurpeljto.gateway.dto.TodayDTOList;
import com.anurpeljto.gateway.dto.WeeklyByTypeDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FiscalizationService {

    void publishReceipt(Receipt receipt);

    ReceiptResponse listReceipts(Integer page, Integer size, String filterBy, String sortBy);

    Receipt getReceiptById(Integer id);

    ReceiptResponse getFiscalizedThisWeek(Integer page, Integer size, String filterBy, String sortBy, Integer tenantId);

    ReceiptResponse getPendingThisWeek(Integer page, Integer size, String filterBy, String sortBy, Integer tenantId);

    ReceiptResponse getCancelledThisWeek(Integer page, Integer size, String filterBy, String sortBy, Integer tenantId);

    TodayDTOList getTodaysTransactions(Integer page, Integer size, String filterBy, String sortBy, Integer tenantId);

    WeeklyByTypeDTO getWeeklyByType(Integer tenantId);

    Integer getTodaysTransactionsCount();

    Integer getWeeklyTransactionsCount();

    Integer getMonthlyTransactionsCount();

    Float getAverageReceiptsPerDay();
}
