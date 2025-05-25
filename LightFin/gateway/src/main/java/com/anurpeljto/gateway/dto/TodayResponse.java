package com.anurpeljto.gateway.dto;

import java.util.List;

public class TodayResponse {
    private List<TodayDTO> todayDTOList;

    public TodayResponse(List<TodayDTO> todayDTOList) {
        this.todayDTOList = todayDTOList;
    }

    public List<TodayDTO> getTodayDTOList() {
        return todayDTOList;
    }
}
