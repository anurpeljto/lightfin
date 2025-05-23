package com.anurpeljto.fiscalizationlistener.dto;

import com.anurpeljto.fiscalizationlistener.domain.Receipt;

import java.util.List;

public class ReceiptResponseDTO {

    private List<Receipt> data;
    private int count;

    public ReceiptResponseDTO(List<Receipt> data) {
        this.data = data;
        this.count = data.size();
    }

    public List<Receipt> getData() {
        return data;
    }

    public int getCount() {
        return count;
    }
}
