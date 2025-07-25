package com.example.travel.planner.service;

import com.example.travel.planner.dto.activity.ActivityCreateDTO;
import com.example.travel.planner.dto.activity.ActivityUpdateDTO;
import com.example.travel.planner.dto.trip.TripCreateDTO;
import com.example.travel.planner.dto.trip.TripDTO;
import com.example.travel.planner.dto.trip.TripUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface TripService {
    Page<TripDTO> findAllTrips(int page, int size);

    TripDTO findTripById(Authentication authentication, String id);

    TripDTO createTrip(Authentication authentication, TripCreateDTO tripCreateDTO);

    TripDTO updateTripById(Authentication authentication,String id, TripUpdateDTO tripUpdateDTO);

    void deleteTripById(Authentication authentication, String id);

    TripDTO addActivityToTrip(Authentication authentication, String tripId, ActivityCreateDTO activityCreateDTO);

    void deleteActivityById(Authentication authentication, String tripId, String activityId);

    TripDTO updateActivityById(Authentication authentication, String tripId, String activityId, ActivityUpdateDTO activityUpdateDTO);
}
