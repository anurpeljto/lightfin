package com.anurpeljto.fiscalizationlistener.services;

import com.anurpeljto.fiscalizationlistener.domain.Receipt;

public interface FiscalizationService {

    void fiscalize(Receipt receipt);

    void saveToDatabase(Receipt receipt);
}
