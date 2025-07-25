package com.example.travel.planner.testdata;

import com.example.travel.planner.dto.trip.TripCreateDTO;
import com.example.travel.planner.dto.trip.TripDTO;
import com.example.travel.planner.dto.trip.TripUpdateDTO;
import com.example.travel.planner.entity.Trip;

public class TestTripProvider {

    public static final String TRIP_ID = "trip-123";

    public static Trip createDefaultTrip() {
        return Trip.builder()
                .id(TRIP_ID)
                .userId("user-123")
                .title("Test Trip")
                .build();
    }

    public static TripDTO createTripDTO() {
        return TripDTO.builder()
                .id(TRIP_ID)
                .title("Test Trip")
                .build();
    }

    public static TripCreateDTO createTripCreateDTO() {
        return TripCreateDTO.builder()
                .title("Test Trip")
                .build();
    }

    public static TripUpdateDTO createTripUpdateDTO() {
        return TripUpdateDTO.builder()
                .title("Updated Trip")
                .build();
    }
}
