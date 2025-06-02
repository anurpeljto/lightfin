package com.anurpeljto.subsidylistener.services.impl;

import com.anurpeljto.subsidylistener.domain.Subsidy;
import com.anurpeljto.subsidylistener.exceptions.SubsidyMissingException;
import com.anurpeljto.subsidylistener.model.SubsidyStatus;
import com.anurpeljto.subsidylistener.repositories.SubsidyRepository;
import com.anurpeljto.subsidylistener.services.SubsidyService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubsidyServiceImpl implements SubsidyService {

    private final SubsidyRepository subsidyRepository;

    public SubsidyServiceImpl(final SubsidyRepository subsidyRepository){
        this.subsidyRepository = subsidyRepository;
    }

    @Override
    public void saveSubsidy(Subsidy subsidy){
        subsidyRepository.save(subsidy);
    }

    @Override
    public void deleteSubsidy(Integer id){
        if(id == null){
            throw new IllegalArgumentException("ID cannot be null!");
        }
        subsidyRepository.deleteById(id);
    }

    @Override
    public void approveSubsidy(Integer id){
        if(id == null){
            throw new IllegalArgumentException("Subsidy ID cannot be null!");
        }
        Subsidy subsidy = subsidyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subsidy with given ID does not exist!"));

        if (subsidy.getStatus() != SubsidyStatus.PENDING) {
            throw new IllegalStateException(
                    "Subsidy must be in PENDING status to be approved. Current status: " + subsidy.getStatus()
            );
        }

        subsidy.setStatus(SubsidyStatus.APPROVED);
        Date now = new Date();
        subsidy.setApprovalDate(now);
        subsidyRepository.save(subsidy);
    }

    @Override
    public void rejectSubsidy(Integer id){
        if(id == null){
            throw new IllegalArgumentException("Subsidy ID cannot be null!");
        }
        Subsidy subsidy = subsidyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subsidy with given ID does not exist!"));

        if (subsidy.getStatus() != SubsidyStatus.PENDING) {
            throw new IllegalStateException(
                    "Subsidy must be in PENDING status to be rejected. Current status: " + subsidy.getStatus()
            );
        }

        subsidy.setStatus(SubsidyStatus.REJECTED);
        subsidyRepository.save(subsidy);
    }

    @Override
    public List<Subsidy> getSubsidies() {
        return subsidyRepository.findAll();
    }

    @Override
    public Subsidy getSubsidyById(Integer id) {
        return subsidyRepository.findById(id).orElseThrow(() -> new SubsidyMissingException());
    }

    @Override
    public Page<Subsidy> getSubsidiesByUserId(Integer id, Pageable pageable){
        return subsidyRepository.findByRecipientId(id, pageable);
    }
}
