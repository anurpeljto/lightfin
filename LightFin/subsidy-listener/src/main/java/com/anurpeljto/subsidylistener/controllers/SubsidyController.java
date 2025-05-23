package com.anurpeljto.subsidylistener.controllers;

import com.anurpeljto.subsidylistener.domain.Subsidy;
import com.anurpeljto.subsidylistener.services.SubsidyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubsidyController {

    private final SubsidyService subsidyService;

    public SubsidyController(SubsidyService subsidyService) {
        this.subsidyService = subsidyService;
    }

    @GetMapping(path = "/list")
    public List<Subsidy> getSubsidies() {
        return subsidyService.getSubsidies();
    }

    @GetMapping(path = "/subsidy/{id}")
    public Subsidy getSubsidy(@PathVariable("id") Integer id) {
        return subsidyService.getSubsidyById(id);
    }
}
