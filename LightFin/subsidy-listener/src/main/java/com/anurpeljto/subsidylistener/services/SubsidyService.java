package com.anurpeljto.subsidylistener.services;

import com.anurpeljto.subsidylistener.domain.Subsidy;

public interface SubsidyService {

    void saveSubsidy(Subsidy subsidy);

    void deleteSubsidy(Integer id);

    void approveSubsidy(Integer id);

    void rejectSubsidy(Integer id);
}
