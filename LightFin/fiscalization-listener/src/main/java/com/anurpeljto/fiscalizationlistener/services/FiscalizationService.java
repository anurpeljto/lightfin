package com.anurpeljto.fiscalizationlistener.services;

import com.anurpeljto.fiscalizationlistener.domain.Receipt;
import com.anurpeljto.fiscalizationlistener.domain.WeeklyByType;
import com.anurpeljto.fiscalizationlistener.dto.ReceiptResponseDTO;
import com.anurpeljto.fiscalizationlistener.dto.TodayDTO;
import com.anurpeljto.fiscalizationlistener.dto.TodayDTOList;
import com.anurpeljto.fiscalizationlistener.dto.WeeklyByTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FiscalizationService {

    void sendToFiscalize(Integer id);

    Receipt saveToDatabase(Receipt receipt);

    Page<Receipt> getReceipts(Pageable pageable);

    Receipt getReceipt(Integer id);

    Page<Receipt> fiscalizedReceiptsThisWeek(Pageable pageable, Integer tenantId);

    Page<Receipt> pendingReceiptsThisWeek(Pageable pageable, Integer tenantId);

    Page<Receipt> cancelledReceiptsThisWeek(Pageable pageable, Integer tenantId);

    TodayDTOList getTodaysTransactions(Integer tenantId);

    WeeklyByTypeDTO getWeeklyByType(Integer tenantId);

    Integer todaysTransactionsCount();

    Integer weeklyTransactionsCount();

    Integer monthlyTransactionsCount();

    byte[] generateReceipt(Integer id);

    Float averageReceiptsPerDay();
}
