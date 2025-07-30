package com.example.travel.planner.controller;

import com.example.travel.planner.dto.activity.ActivityCreateDTO;
import com.example.travel.planner.dto.activity.ActivityUpdateDTO;
import com.example.travel.planner.dto.trip.TripCreateDTO;
import com.example.travel.planner.dto.trip.TripDTO;
import com.example.travel.planner.dto.trip.TripUpdateDTO;
import com.example.travel.planner.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trips")
public class TripController {

    private final TripService tripService;

    @GetMapping
    public Page<TripDTO> getAllTrips(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {

        return tripService.findAllTrips(page, size);
    }

    @GetMapping("/{id}")
    public TripDTO getTripById(Authentication authentication, @PathVariable String id) {

        return tripService.findTripById(authentication, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TripDTO createTrip(Authentication authentication, @Valid @RequestBody TripCreateDTO tripCreateDTO) {
        return tripService.createTrip(authentication, tripCreateDTO);
    }

    @PutMapping("/{id}")
    public TripDTO updateTrip(Authentication authentication, @PathVariable String id,
                              @Valid @RequestBody TripUpdateDTO tripUpdateDTO) {

        return tripService.updateTripById(authentication, id, tripUpdateDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTrip(Authentication authentication, @PathVariable String id) {
        tripService.deleteTripById(authentication, id);
    }

    @PostMapping("/{tripId}/activities")
    @ResponseStatus(value = HttpStatus.CREATED)
    public TripDTO addActivityToTrip(Authentication authentication, @PathVariable String tripId,
                                     @Valid @RequestBody ActivityCreateDTO activityCreateDTO) {

        return tripService.addActivityToTrip(authentication, tripId, activityCreateDTO);
    }

    @PatchMapping("/{tripId}/activities/{activityId}")
    public TripDTO updateActivity(Authentication authentication, @PathVariable String tripId, @PathVariable String activityId, @Valid @RequestBody ActivityUpdateDTO activityUpdateDTO) {

        return tripService.updateActivityById(authentication, tripId, activityId, activityUpdateDTO);
    }

    @DeleteMapping("/{tripId}/activities/{activityId}")
    public void deleteActivity(Authentication authentication, @PathVariable String tripId, @PathVariable String activityId) {

        tripService.deleteActivityById(authentication, tripId, activityId);
    }

}

