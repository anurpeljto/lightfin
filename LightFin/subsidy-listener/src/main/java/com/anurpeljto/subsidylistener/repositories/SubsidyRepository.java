package com.anurpeljto.subsidylistener.repositories;

import com.anurpeljto.subsidylistener.domain.Subsidy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubsidyRepository extends JpaRepository<Subsidy, Integer> {
}
