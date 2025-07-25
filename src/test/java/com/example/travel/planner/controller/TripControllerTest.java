package com.example.travel.planner.controller;

import com.example.travel.planner.controller.TripController;
import com.example.travel.planner.dto.activity.ActivityCreateDTO;
import com.example.travel.planner.dto.trip.TripCreateDTO;
import com.example.travel.planner.dto.trip.TripDTO;
import com.example.travel.planner.dto.trip.TripUpdateDTO;
import com.example.travel.planner.service.TripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripControllerTest {

    @Mock
    private TripService tripService;

    @InjectMocks
    private TripController tripController;

    @Mock
    private Authentication authentication;

    private TripCreateDTO tripCreateDTO;
    private TripUpdateDTO tripUpdateDTO;
    private ActivityCreateDTO activityCreateDTO;
    private TripDTO tripDTO;

    @BeforeEach
    void setup() {
        tripCreateDTO = new TripCreateDTO();
        tripUpdateDTO = new TripUpdateDTO();
        activityCreateDTO = new ActivityCreateDTO();

        tripDTO = new TripDTO();
        tripDTO.setId("1");
    }

    @Test
    void findAllTrips_ShouldCallService() {
        when(tripService.findAllTrips()).thenReturn(List.of(tripDTO));

        List<TripDTO> result = tripController.getAllTrips();

        assertEquals(1, result.size());
        verify(tripService).findAllTrips();
    }

    @Test
    void findTripById_ShouldCallService() {
        when(tripService.findTripById(authentication, "123")).thenReturn(tripDTO);

        TripDTO result = tripController.getTripById(authentication, "123");

        assertEquals("1", result.getId());
        verify(tripService).findTripById(authentication, "123");
    }

    @Test
    void createTrip_ShouldCallService() {
        when(tripService.createTrip(authentication, tripCreateDTO)).thenReturn(tripDTO);

        TripDTO result = tripController.createTrip(authentication, tripCreateDTO);

        assertEquals("1", result.getId());
        verify(tripService).createTrip(authentication, tripCreateDTO);
    }

    @Test
    void updateTrip_ShouldCallService() {
        when(tripService.updateTripById(authentication, "1", tripUpdateDTO)).thenReturn(tripDTO);

        TripDTO result = tripController.updateTrip(authentication, "1", tripUpdateDTO);

        assertEquals("1", result.getId());
        verify(tripService).updateTripById(authentication, "1", tripUpdateDTO);
    }

    @Test
    void deleteTrip_ShouldCallService() {
        when(tripService.deleteTripById(authentication, "1")).thenReturn("deleted");

        String result = tripController.deleteTrip(authentication, "1");

        assertEquals("deleted", result);
        verify(tripService).deleteTripById(authentication, "1");
    }

    @Test
    void addActivity_ShouldCallService() {
        when(tripService.addActivityToTrip(authentication, "1", activityCreateDTO)).thenReturn(tripDTO);

        TripDTO result = tripController.addActivityToTrip(authentication, "1", activityCreateDTO);

        assertEquals("1", result.getId());
        verify(tripService).addActivityToTrip(authentication, "1", activityCreateDTO);
    }

    @Test
    void updateActivity_ShouldCallService() {
        when(tripService.updateActivityById(authentication, "1", "a1", activityCreateDTO)).thenReturn(tripDTO);

        TripDTO result = tripController.updateActivity(authentication, "1", "a1", activityCreateDTO);

        assertEquals("1", result.getId());
        verify(tripService).updateActivityById(authentication, "1", "a1", activityCreateDTO);
    }

    @Test
    void deleteActivity_ShouldCallService() {
        when(tripService.deleteActivityById(authentication, "1", "a1")).thenReturn("deleted");

        String result = tripController.deleteActivity(authentication, "1", "a1");

        assertEquals("deleted", result);
        verify(tripService).deleteActivityById(authentication, "1", "a1");
    }
}
