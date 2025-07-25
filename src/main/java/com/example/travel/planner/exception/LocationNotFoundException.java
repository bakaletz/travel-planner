package com.example.travel.planner.exception;

public class LocationNotFoundException extends RuntimeException {

    public LocationNotFoundException(String locationQuery) {
        super(String.format(
                "No response for query: \"%s\". Expected format: '<City> <Country>', '<Place first word> <Place second word>' or similar.",
                locationQuery
        ));
    }
}
