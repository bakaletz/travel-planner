package com.example.travel.planner.exception;

public class ExternalApiException extends RuntimeException {
    public ExternalApiException(String customMessage) {
        super(customMessage);
    }
}
