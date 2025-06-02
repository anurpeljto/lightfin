package com.anurpeljto.subsidylistener.repositories;

import com.anurpeljto.subsidylistener.domain.Subsidy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubsidyRepository extends JpaRepository<Subsidy, Integer> {
    Page<Subsidy> findByRecipientId(Integer id, Pageable pageable);
}
