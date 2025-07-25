package com.example.travel.planner.config;

import com.example.travel.planner.entity.Trip;
import com.example.travel.planner.exception.ResourceNotFoundException;
import com.example.travel.planner.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TripSecurity {

    private final TripRepository tripRepository;

    public boolean isOwner(String tripId, Authentication authentication) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new ResourceNotFoundException("Trip", tripId));
        return trip.getUserId().equals(authentication.getName());
    }
}
