package com.anurpeljto.fiscalizationlistener.domain;

import lombok.*;

import java.util.Objects;

public class WeeklyByType {

    private String label;
    private Integer value;
    private String color;

    public WeeklyByType(String label, Long value) {
        this.label = label;
        this.value = value.intValue();
        if(Objects.equals(this.label, "CASH")){
            this.color = "hsl(355, 70%, 50%)";
        }
        else if(Objects.equals(this.label, "CREDIT")){
            this.color = "hsl(295, 70%, 50%)";
        }
    }

    public String getLabel() {
        return label;
    }

    public String getColor() {
        return color;
    }

    public Integer getValue() {
        return value;
    }
}
