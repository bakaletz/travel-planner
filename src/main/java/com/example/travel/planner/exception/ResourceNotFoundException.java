package com.example.travel.planner.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceType, String id) {
        super(String.format("%s with id: %s does not exist", resourceType, id));
    }

    public ResourceNotFoundException(String resourceType, String resourceItem, String id) {
        super(String.format("%s with %s: %s does not exist", resourceType, resourceItem, id));
    }
}
