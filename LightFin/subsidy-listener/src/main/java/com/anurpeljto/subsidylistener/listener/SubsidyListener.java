package com.anurpeljto.subsidylistener.listener;

import com.anurpeljto.subsidylistener.domain.Subsidy;
import com.anurpeljto.subsidylistener.services.SubsidyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class SubsidyListener {

    private final ObjectMapper objectMapper;

    private final SubsidyService subsidyService;

    public SubsidyListener(final ObjectMapper objectMapper, final SubsidyService subsidyService){
        this.objectMapper = objectMapper;
        this.subsidyService = subsidyService;
    }

    @KafkaListener(topics = "subsidy.published")
    public String listensToPublish(String payload){
        try{
            log.info("Received subsidy: {}", payload);
            Subsidy subsidy = objectMapper.readValue(payload, Subsidy.class);
            subsidyService.saveSubsidy(subsidy);
        } catch (JsonProcessingException ex){
            throw new RuntimeException(ex);
        }
        return payload;
    }

    @KafkaListener(topics = "subsidy.approved")
    public String approveSubsidy(String id){
        log.info("Approved subisdy with id: {}", id);
        Integer number = Integer.parseInt(id);
        subsidyService.approveSubsidy(number);
        return id;
    }

    @KafkaListener(topics = "subsidy.rejected")
    public String rejectSubsidy(String id){
        log.info("Rejected subisdy with id: {}", id);
        Integer number = Integer.parseInt(id);
        subsidyService.rejectSubsidy(number);
        return id;
    }

    @KafkaListener(topics = "subsidy.deleted")
    public String deleteSubsidy(String id){
        log.info("Deleted subisdy with id: {}", id);
        Integer number = Integer.parseInt(id);
        subsidyService.deleteSubsidy(number);
        return id;
    }
}
