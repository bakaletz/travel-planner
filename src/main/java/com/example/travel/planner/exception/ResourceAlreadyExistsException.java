package com.example.travel.planner.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String resourceType, String resourceItem, String value) {
        super(String.format("%s with %s: %s already exist", resourceType, resourceItem, value));
    }
}
