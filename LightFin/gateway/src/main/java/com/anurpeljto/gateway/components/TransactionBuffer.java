package com.anurpeljto.gateway.components;

import com.anurpeljto.gateway.domain.fiscalization.Receipt;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Component
public class TransactionBuffer {
    private final Deque<Receipt> buffer = new ArrayDeque<>();
    private final int maxSize = 100;

    public synchronized void addTransaction(Receipt receipt) {
        if(buffer.size() >= maxSize) buffer.removeFirst();
        buffer.addLast(receipt);
    }

    public synchronized List<Receipt> getTransactions() {
        return new ArrayList<>(buffer);
    }
}
