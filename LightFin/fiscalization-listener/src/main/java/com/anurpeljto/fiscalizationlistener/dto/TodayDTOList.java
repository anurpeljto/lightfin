package com.anurpeljto.fiscalizationlistener.dto;

import org.springframework.data.domain.Page;

import java.util.List;
public class TodayDTOList {

    private List<TodayDTO> todayDTOList;

    public TodayDTOList(List<TodayDTO> todayDTOList) {
        this.todayDTOList = todayDTOList;
    }

    public List<TodayDTO> getTodayDTOList() {
        return todayDTOList;
    }
}
