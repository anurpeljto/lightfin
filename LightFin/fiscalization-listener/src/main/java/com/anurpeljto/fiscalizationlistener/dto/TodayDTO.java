package com.anurpeljto.fiscalizationlistener.dto;

import java.util.List;

public class TodayDTO {

    private String status;
    private Integer count;

    public TodayDTO(String status, Long count) {
        this.status = status;
        this.count = count.intValue();
    }

    public String getStatus() {
        return status;
    }

    public Integer getCount() {
        return count;
    }
}
