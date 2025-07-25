package com.example.travel.planner.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String resourceType, String resourceItem, String value) {
        super(resourceType + " with "+ resourceItem +": " + value + " already exist");
    }
}
