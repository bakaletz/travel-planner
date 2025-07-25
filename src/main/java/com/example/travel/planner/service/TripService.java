package com.example.travel.planner.service;

import com.example.travel.planner.dto.activity.ActivityCreateDTO;
import com.example.travel.planner.dto.trip.TripCreateDTO;
import com.example.travel.planner.dto.trip.TripDTO;
import com.example.travel.planner.dto.trip.TripUpdateDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TripService {

    List<TripDTO> findAllTrips();

    TripDTO findTripById(Authentication authentication, String id);

    TripDTO createTrip(Authentication authentication, TripCreateDTO tripCreateDTO);

    TripDTO updateTripById(Authentication authentication,String id, TripUpdateDTO tripUpdateDTO);

    String deleteTripById(Authentication authentication, String id);

    TripDTO addActivityToTrip(Authentication authentication, String tripId, ActivityCreateDTO activityCreateDTO);

    String deleteActivityById(Authentication authentication, String tripId, String activityId);

    TripDTO updateActivityById(Authentication authentication, String tripId, String activityId, ActivityCreateDTO activityCreateDTO);
}
