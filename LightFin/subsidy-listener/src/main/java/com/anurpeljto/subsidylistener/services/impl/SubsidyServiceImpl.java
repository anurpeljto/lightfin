package com.anurpeljto.subsidylistener.services.impl;

import com.anurpeljto.subsidylistener.domain.Subsidy;
import com.anurpeljto.subsidylistener.exceptions.SubsidyMissingException;
import com.anurpeljto.subsidylistener.model.SubsidyStatus;
import com.anurpeljto.subsidylistener.repositories.SubsidyRepository;
import com.anurpeljto.subsidylistener.services.SubsidyService;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.lowagie.text.Document;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
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

        subsidy.setStatus(SubsidyStatus.ACCEPTED);
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

    @Override
    public byte[] generateSubsidyReport(Integer id, String first_name, String last_name, String email){
        List<Subsidy> subsidies = subsidyRepository.findAll();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        document.add(new Paragraph("Subsidy report for " + first_name + " " + last_name));
        Paragraph emailParagraph = new Paragraph("Email: " + email);
        emailParagraph.setSpacingAfter(10f);
        document.add(emailParagraph);

        PdfPTable table = new PdfPTable(5);
        table.addCell("Subsidy ID");
        table.addCell("Amount");
        table.addCell("Status");
        table.addCell("Approved Date");
        table.addCell("Valid Until");

        for (Subsidy subsidy : subsidies) {
            table.addCell(String.valueOf(subsidy.getId()));
            table.addCell(String.valueOf(subsidy.getAmount()));
            table.addCell(String.valueOf(subsidy.getStatus()));
            table.addCell(String.valueOf(subsidy.getApprovalDate()));
            table.addCell(String.valueOf(subsidy.getValid_until()));
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }
}
