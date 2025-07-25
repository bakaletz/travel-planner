package com.example.travel.planner.config;

import com.example.travel.planner.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TripSecurity {

    private final TripRepository tripRepository;

    public boolean isOwner(String tripId, Authentication authentication) {
        return tripRepository.findById(tripId)
                .map(trip -> trip.getUserId().equals(authentication.getName()))
                .orElseThrow(() -> new AccessDeniedException("No permission"));
    }
}
