package com.anurpeljto.fiscalizationlistener.repositories;

import com.anurpeljto.fiscalizationlistener.domain.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FiscalizationRepository extends JpaRepository<Receipt, String> {
}
