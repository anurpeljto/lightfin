package com.anurpeljto.gateway.services;

import com.anurpeljto.gateway.domain.subsidy.Subsidy;
import com.anurpeljto.gateway.dto.SubsidyPageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SubsidyService {

    List<Subsidy> listSubsidies(Integer page, Integer size);

    Subsidy getSubsidyById(Integer id);

    void publishSubsidy(Subsidy subsidy);

    void deleteSubsidy(Integer id);

    void approveSubsidy(Integer id);

    void rejectSubsidy(Integer id);

    SubsidyPageDTO getSubsidiesByUserId(Integer customerId, Integer page, Integer size, String filterBy, String sortBy);
}
