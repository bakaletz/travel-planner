package com.example.travel.planner.exception;

import org.springframework.web.client.RestClientException;

public class ExternalApiException extends RuntimeException {
    public ExternalApiException(String customMessage, RestClientException restClientException) {
        super(customMessage + restClientException);
    }
}
