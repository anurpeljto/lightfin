package com.anurpeljto.fiscalizationlistener.dto;

import com.anurpeljto.fiscalizationlistener.domain.WeeklyByType;

import java.util.List;


public class WeeklyByTypeDTO {

    private List<WeeklyByType> weeklyByType;

    public WeeklyByTypeDTO(List<WeeklyByType> weeklyByType) {
        this.weeklyByType = weeklyByType;
    }

    public List<WeeklyByType> getWeeklyByType() {
        return weeklyByType;
    }
}
