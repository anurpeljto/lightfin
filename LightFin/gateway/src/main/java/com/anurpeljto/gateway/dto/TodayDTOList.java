package com.anurpeljto.gateway.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TodayDTOList {

    private List<TodayDTO> todayDTOList;
    private long totalElements;
    private int totalPages;
    private int number;
    private int size;

    public TodayDTOList(Page<TodayDTO> page) {
        this.todayDTOList = page.getContent();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.number = page.getNumber();
        this.size = page.getSize();
    }
}