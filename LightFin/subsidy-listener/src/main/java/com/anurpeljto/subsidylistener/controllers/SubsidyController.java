package com.anurpeljto.subsidylistener.controllers;

import com.anurpeljto.subsidylistener.domain.Subsidy;
import com.anurpeljto.subsidylistener.dto.SubsidyPageDTO;
import com.anurpeljto.subsidylistener.services.SubsidyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class SubsidyController {

    private final SubsidyService subsidyService;

    public SubsidyController(SubsidyService subsidyService) {
        this.subsidyService = subsidyService;
    }

    @GetMapping(path = "/list")
    public SubsidyPageDTO getSubsidies(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Subsidy> subsidies = subsidyService.getSubsidies(pageable);
        return new SubsidyPageDTO(subsidies);
    }

    @GetMapping(path = "/subsidy/{id}")
    public Subsidy getSubsidy(@PathVariable("id") Integer id) {
        return subsidyService.getSubsidyById(id);
    }

    @GetMapping(path = "/subsidy/user/{id}")
    public SubsidyPageDTO getSubsidiesByUserId(
            @PathVariable("id") Integer id,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam("filterBy") String filterBy,
            @RequestParam("sortBy") String sortBy) {

        String sortField = (filterBy == null || filterBy.isBlank()) ? "id" : filterBy;
        Sort.Direction direction;
        try {
            direction = Sort.Direction.valueOf((sortBy == null) ? "DESC" : sortBy.toUpperCase());
        } catch (IllegalArgumentException e) {
            direction = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<Subsidy> subsidies = subsidyService.getSubsidiesByUserId(id, pageable);
        return new SubsidyPageDTO(subsidies);
    }

    @GetMapping(path = "/user/{id}/report", produces = "application/pdf")
    public ResponseEntity<byte[]> getSubsidyReport(
            @PathVariable final Integer id,
            @RequestParam final String first_name,
            @RequestParam final String last_name,
            @RequestParam final String email
    ) {
        byte[] pdfFile =  subsidyService.generateSubsidyReport(id, first_name, last_name, email);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("subsidy_report_user" + first_name + "_" + last_name + ".pdf")
                .build());

        return new ResponseEntity<>(pdfFile, headers, HttpStatus.OK);
    }

    @GetMapping(path="/pending")
    public SubsidyPageDTO getPendingSubsidies(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Subsidy> subsidies = subsidyService.getPendingSubsidies(pageable);
        return new SubsidyPageDTO(subsidies);
    }

    @GetMapping("/week")
    public SubsidyPageDTO getWeeklySubsidies(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Subsidy> subsidies = subsidyService.getWeeklySubsidies(pageable);
        return new SubsidyPageDTO(subsidies);
    }
}
