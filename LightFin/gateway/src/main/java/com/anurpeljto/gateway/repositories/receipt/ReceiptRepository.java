package com.anurpeljto.gateway.repositories.receipt;

import com.anurpeljto.gateway.domain.fiscalization.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {
}
