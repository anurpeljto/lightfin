package com.anurpeljto.fiscalizationlistener.services;

import com.anurpeljto.fiscalizationlistener.domain.Receipt;
import com.anurpeljto.fiscalizationlistener.dto.ReceiptResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FiscalizationService {

    void sendToFiscalize(Integer id);

    Receipt saveToDatabase(Receipt receipt);

    List<Receipt> getReceipts(Pageable pageable);

    Receipt getReceipt(Integer id);

    ReceiptResponseDTO fiscalizedReceiptsThisWeek();
}
