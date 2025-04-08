package com.anurpeljto.gateway.repositories;

import com.anurpeljto.gateway.domain.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {
}
