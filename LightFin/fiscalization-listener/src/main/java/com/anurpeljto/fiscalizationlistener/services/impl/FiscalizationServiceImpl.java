package com.anurpeljto.fiscalizationlistener.services.impl;

import com.anurpeljto.fiscalizationlistener.domain.Item;
import com.anurpeljto.fiscalizationlistener.domain.Receipt;
import com.anurpeljto.fiscalizationlistener.repositories.FiscalizationRepository;
import com.anurpeljto.fiscalizationlistener.services.FiscalizationService;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
public class FiscalizationServiceImpl implements FiscalizationService {

    private final FiscalizationRepository fiscalizationRepository;

    public FiscalizationServiceImpl(FiscalizationRepository fiscalizationRepository){
        this.fiscalizationRepository = fiscalizationRepository;
    }

    @Override
    public void fiscalize(Receipt receipt){
        OffsetDateTime now = OffsetDateTime.now();
        receipt.setTimestamp(now);

        String signatureInput = receipt.getItems().toString() + receipt.getTotal() + now.toString();
        String signature = Hashing.sha256()
                .hashString(signatureInput, StandardCharsets.UTF_8)
                .toString();
        receipt.setSignature(signature);

        String fiscalCode = "MOCK" + Integer.toHexString(signature.hashCode()).toUpperCase();
        receipt.setFiscalCode(fiscalCode);

        receipt.setStatus("FISCALIZED");

        saveToDatabase(receipt);
    }

    @Override
    public void saveToDatabase(Receipt receipt){
        this.fiscalizationRepository.save(receipt);
    }
}
