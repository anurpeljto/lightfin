package com.anurpeljto.gateway.services;

import com.anurpeljto.gateway.domain.subsidy.Subsidy;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SubsidyService {

    List<Subsidy> listSubsidies(Pageable pageable);

    Optional<Subsidy> getSubsidyById(Integer id);

    void publishSubsidy(Subsidy subsidy);

    void deleteSubsidy(Integer id);

    void approveSubsidy(Integer id);

    void rejectSubsidy(Integer id);
}
