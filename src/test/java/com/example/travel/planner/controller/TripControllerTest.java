package com.example.travel.planner.controller;

import com.example.travel.planner.dto.activity.ActivityCreateDTO;
import com.example.travel.planner.dto.activity.ActivityUpdateDTO;
import com.example.travel.planner.dto.trip.TripCreateDTO;
import com.example.travel.planner.dto.trip.TripUpdateDTO;
import com.example.travel.planner.service.TripService;
import com.example.travel.planner.testdata.TestActivityProvider;
import com.example.travel.planner.testdata.TestTripProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TripControllerTest {

    @Mock
    private TripService tripService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TripController tripController;

    private final String tripId = TestTripProvider.TRIP_ID;
    private final String activityId = TestActivityProvider.ACTIVITY_ID;

    private TripCreateDTO testTripCreateDTO;
    private TripUpdateDTO testTripUpdateDTO;
    private ActivityCreateDTO testActivityCreateDTO;
    private ActivityUpdateDTO testActivityUpdateDTO;

    @BeforeEach
    void setup() {
        testTripCreateDTO = TestTripProvider.createTripCreateDTO();
        testTripUpdateDTO = TestTripProvider.createTripUpdateDTO();
        testActivityCreateDTO = TestActivityProvider.createActivityCreateDTO();
        testActivityUpdateDTO = TestActivityProvider.createActivityUpdateDTO();
    }

    @Test
    void getAllTrips_ShouldVerify() {
        tripController.getAllTrips(0, 10);

        verify(tripService).findAllTrips(0, 10);

    }

    @Test
    void getTripById_ShouldVerify() {
        tripController.getTripById(authentication, tripId);

        verify(tripService).findTripById(authentication, tripId);
    }

    @Test
    void createTrip_ShouldVerify() {
        tripController.createTrip(authentication, testTripCreateDTO);

        verify(tripService).createTrip(authentication, testTripCreateDTO);
    }

    @Test
    void updateTrip_ShouldVerify() {
        tripController.updateTrip(authentication, tripId, testTripUpdateDTO);

        verify(tripService).updateTripById(authentication, tripId, testTripUpdateDTO);
    }

    @Test
    void deleteTrip_ShouldVerify() {
        tripController.deleteTrip(authentication, tripId);

        verify(tripService).deleteTripById(authentication, tripId);
    }

    @Test
    void addActivityToTrip_ShouldVerify() {
        tripController.addActivityToTrip(authentication, tripId, testActivityCreateDTO);

        verify(tripService).addActivityToTrip(authentication, tripId, testActivityCreateDTO);
    }

    @Test
    void updateActivity_ShouldVerify() {
        tripController.updateActivity(authentication, tripId, activityId, testActivityUpdateDTO);

        verify(tripService).updateActivityById(authentication, tripId, activityId, testActivityUpdateDTO);
    }

    @Test
    void deleteActivity_ShouldVerify() {

        tripController.deleteActivity(authentication, tripId, activityId);

        verify(tripService).deleteActivityById(authentication, tripId, activityId);
    }
}
