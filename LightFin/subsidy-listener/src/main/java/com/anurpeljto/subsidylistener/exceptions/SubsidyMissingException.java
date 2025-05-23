package com.anurpeljto.subsidylistener.exceptions;

public class SubsidyMissingException extends RuntimeException {
    public SubsidyMissingException(String message) {
        super(message);
    }

    public SubsidyMissingException() {
        super("Subsidy not found");
    }
}
