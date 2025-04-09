package com.anurpeljto.fiscalizationlistener.services;

import com.anurpeljto.fiscalizationlistener.domain.Receipt;

public interface FiscalizationService {

    void sendToFiscalize(Integer id);

    Receipt saveToDatabase(Receipt receipt);
}
