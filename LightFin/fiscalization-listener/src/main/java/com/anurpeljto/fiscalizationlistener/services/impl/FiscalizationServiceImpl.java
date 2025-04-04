package com.anurpeljto.fiscalizationlistener.services.impl;

import com.anurpeljto.fiscalizationlistener.domain.Receipt;
import com.anurpeljto.fiscalizationlistener.repositories.FiscalizationRepository;
import com.anurpeljto.fiscalizationlistener.services.FiscalizationService;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FiscalizationServiceImpl implements FiscalizationService {

    private final FiscalizationRepository fiscalizationRepository;

    public FiscalizationServiceImpl(FiscalizationRepository fiscalizationRepository){
        this.fiscalizationRepository = fiscalizationRepository;
    }

    @Override
    public void fiscalize(Receipt receipt){
        String generatedId = "R-" + UUID.randomUUID().toString();
        receipt.setId(generatedId);

        LocalDateTime now = LocalDateTime.now();
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
