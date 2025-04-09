package com.anurpeljto.gateway.exceptions;

public class InvalidReceiptException extends RuntimeException {
    public InvalidReceiptException(String message) {
        super(message);
    }

    public InvalidReceiptException(String message, Throwable cause) {
      super(message, cause);
    }
}
