package com.anurpeljto.gateway.services;

import com.anurpeljto.gateway.domain.Receipt;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FiscalizationService {

    void publishReceipt(Receipt receipt);

    List<Receipt> listReceipts(Pageable pageable);
}
