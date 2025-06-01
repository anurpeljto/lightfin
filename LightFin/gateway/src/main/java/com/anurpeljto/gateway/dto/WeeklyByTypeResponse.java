package com.anurpeljto.gateway.dto;

import java.util.Objects;

public class WeeklyByTypeResponse {
    private String label;
    private Integer value;
    private String color;
    private String id;

    public WeeklyByTypeResponse(String label, Long value) {
        this.id = label;
        this.label = label;
        this.value = value.intValue();
        if(Objects.equals(this.label, "CASH")){
            this.color = "hsl(355, 70%, 50%)";
        }
        else if(Objects.equals(this.label, "CREDIT")){
            this.color = "hsl(295, 70%, 50%)";
        }
    }
}
