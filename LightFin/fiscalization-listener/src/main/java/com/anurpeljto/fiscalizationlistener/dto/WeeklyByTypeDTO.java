package com.anurpeljto.fiscalizationlistener.dto;

import com.anurpeljto.fiscalizationlistener.domain.WeeklyByType;
import org.springframework.data.domain.Page;

import java.util.List;


public class WeeklyByTypeDTO {

    private List<WeeklyByType> weeklyByType;

    public WeeklyByTypeDTO(Page<WeeklyByType> weeklyByType) {
        this.weeklyByType = weeklyByType.getContent();
    }

    public WeeklyByTypeDTO(List<WeeklyByType> weeklyByType) {
        this.weeklyByType = weeklyByType;
    }

    public List<WeeklyByType> getWeeklyByType() {
        return weeklyByType;
    }
}
