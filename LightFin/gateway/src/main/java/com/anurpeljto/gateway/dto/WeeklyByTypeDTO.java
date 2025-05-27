package com.anurpeljto.gateway.dto;

import java.util.List;

public class WeeklyByTypeDTO {

    private List<WeeklyByTypeResponse> weeklyByType;

    public WeeklyByTypeDTO() {}

    public WeeklyByTypeDTO(List<WeeklyByTypeResponse> data) {
        this.weeklyByType = data;
    }

    public List<WeeklyByTypeResponse> getWeeklyByType() {
        return weeklyByType;
    }

    public void setWeeklyByType(List<WeeklyByTypeResponse> weeklyByType) {
        this.weeklyByType = weeklyByType;
    }
}
