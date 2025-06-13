package com.anurpeljto.subsidylistener.services;

import com.anurpeljto.subsidylistener.domain.Subsidy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubsidyService {

    void saveSubsidy(Subsidy subsidy);

    void deleteSubsidy(Integer id);

    void approveSubsidy(Integer id);

    void rejectSubsidy(Integer id);

    Page<Subsidy> getSubsidies(Pageable pageable);

    Subsidy getSubsidyById(Integer id);

    Page<Subsidy> getSubsidiesByUserId(Integer id, Pageable pageable);

    byte[] generateSubsidyReport(Integer id, String first_name, String last_name, String email);

    Page<Subsidy> getPendingSubsidies(Pageable pageable);

    Page<Subsidy> getWeeklySubsidies(Pageable pageable);
}
