package com.anurpeljto.fiscalizationlistener.repositories;

import com.anurpeljto.fiscalizationlistener.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}
