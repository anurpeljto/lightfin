package com.anurpeljto.gateway.dto;

import com.anurpeljto.gateway.domain.fiscalization.Receipt;

import java.util.List;

public class ReceiptResponse {
    private List<Receipt> data;
    private int count;

    public ReceiptResponse(List<Receipt> data) {
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
