package com.anurpeljto.gateway.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
@RequestMapping("/api/reports")
public class ReportProxyController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${spring.loans.url}")
    private String url;

    @Value("${spring.subsidy.url}")
    private String subsidiesMicroserviceUrl;

    @Value("${spring.fiscalization.url}")
    private String fiscalizationMicroserviceUrl;

    @GetMapping("/user-loans/{id}/report")
    public void getUserLoanReport(
            @PathVariable Integer id,
            @RequestParam String first_name,
            @RequestParam String last_name,
            @RequestParam String email,
            HttpServletResponse response
    ) throws IOException {
        String microserviceUrl = String.format(
                "%s/user/%d/report?first_name=%s&last_name=%s&email=%s",
                url,
                id,
                first_name,
                last_name,
                email
        );

        ResponseEntity<byte[]> microserviceResponse = restTemplate.exchange(
                microserviceUrl,
                HttpMethod.GET,
                null,
                byte[].class
        );

        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=loan_report_user_" + first_name + "_" + last_name + ".pdf"
        );

        StreamUtils.copy(microserviceResponse.getBody(), response.getOutputStream());
    }

    @GetMapping("/user-subsidies/{id}/report")
    public void getUserSubsidiesReport(
            @PathVariable Integer id,
            @RequestParam String first_name,
            @RequestParam String last_name,
            @RequestParam String email,
            HttpServletResponse response
    ) throws IOException {
        String microserviceUrl = String.format(
                "%s/user/%d/report?first_name=%s&last_name=%s&email=%s",
                subsidiesMicroserviceUrl,
                id,
                first_name,
                last_name,
                email
        );

        ResponseEntity<byte[]> microserviceResponse = restTemplate.exchange(
                microserviceUrl,
                HttpMethod.GET,
                null,
                byte[].class
        );

        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=subsidies_report_user_" + first_name + "_" + last_name + ".pdf"
        );

        StreamUtils.copy(microserviceResponse.getBody(), response.getOutputStream());
    }

    @GetMapping(path = "/receipts/{id}/generate")
    public void generateReceiptReport(
            @PathVariable final Integer id,
            HttpServletResponse response
    ) throws IOException {
        String microserviceUrl = String.format(
                "%s/receipt/%d/generate",
                fiscalizationMicroserviceUrl,
                id
        );

        ResponseEntity<byte[]> microserviceResponse = restTemplate.exchange(
                microserviceUrl,
                HttpMethod.GET,
                null,
                byte[].class
        );

        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=receipt_" + id + "_report_" + ".pdf"
        );

        StreamUtils.copy(microserviceResponse.getBody(), response.getOutputStream());
    }
}
