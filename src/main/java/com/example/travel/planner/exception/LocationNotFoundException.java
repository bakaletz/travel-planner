package com.example.travel.planner.exception;

public class LocationNotFoundException extends RuntimeException {

    public LocationNotFoundException(String locationQuery) {
        super("No response for query: \"" + locationQuery + "\". Expected format: '<City> <Country>'," +
                " '<Place fist word> <Place second word>' or similar.");

    }
}
