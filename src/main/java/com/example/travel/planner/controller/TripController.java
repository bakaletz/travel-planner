package com.example.travel.planner.controller;


import com.example.travel.planner.dto.activity.ActivityCreateDTO;
import com.example.travel.planner.dto.trip.TripCreateDTO;
import com.example.travel.planner.dto.trip.TripDTO;
import com.example.travel.planner.dto.trip.TripUpdateDTO;
import com.example.travel.planner.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trips")
public class TripController {

    private final TripService tripService;


    //Only for development
    @GetMapping
    public List<TripDTO> getAllTrips() {

        return tripService.findAllTrips();
    }


    @GetMapping("/{id}")
    public TripDTO getTripById(Authentication authentication, @PathVariable String id){

        return tripService.findTripById(authentication, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TripDTO createTrip(Authentication authentication,
                                              @Valid @RequestBody TripCreateDTO tripCreateDTO) {

        return tripService.createTrip(authentication, tripCreateDTO);

    }

    @PutMapping("/{id}")
    public TripDTO updateTrip (Authentication authentication, @PathVariable String id,@Valid @RequestBody TripUpdateDTO tripUpdateDTO){

        return tripService.updateTripById(authentication, id, tripUpdateDTO);
    }

    @DeleteMapping("/{id}")
    public String deleteTrip (Authentication authentication, @PathVariable String id){
        return tripService.deleteTripById(authentication, id);
    }

    @PostMapping("/{tripId}/activities")
    @ResponseStatus(value = HttpStatus.CREATED)
    public TripDTO addActivityToTrip(
            Authentication authentication,
            @PathVariable String tripId,
          @Valid @RequestBody ActivityCreateDTO activityCreateDTO) {

        return tripService.addActivityToTrip(authentication, tripId, activityCreateDTO);
    }

    @PatchMapping("/{tripId}/activities/{activityId}")
    public TripDTO updateActivity(Authentication authentication,@PathVariable String tripId,
                                                  @PathVariable String activityId, @Valid @RequestBody ActivityCreateDTO activityCreateDTO){

        return tripService.updateActivityById(authentication, tripId,activityId,activityCreateDTO);
    }

    @DeleteMapping("/{tripId}/activities/{activityId}")
    public String deleteActivity(Authentication authentication, @PathVariable String tripId, @PathVariable String activityId){

        return tripService.deleteActivityById(authentication, tripId,activityId);
    }

}

