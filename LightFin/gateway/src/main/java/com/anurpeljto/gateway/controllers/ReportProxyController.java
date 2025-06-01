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
}
