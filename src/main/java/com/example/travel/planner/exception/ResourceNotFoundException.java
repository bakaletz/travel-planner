package com.example.travel.planner.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceType, String id)
    {
      super(resourceType + " with id: " + id + " does not exist");
    }
    public ResourceNotFoundException(String resourceType, String resourceItem, String id)
    {
        super(resourceType + " with " + resourceItem+ ": " + id + " does not exist");
    }
}
