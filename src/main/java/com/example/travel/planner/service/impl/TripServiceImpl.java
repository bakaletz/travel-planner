package com.example.travel.planner.service.impl;

import com.example.travel.planner.dto.activity.ActivityCreateDTO;
import com.example.travel.planner.dto.activity.ActivityUpdateDTO;
import com.example.travel.planner.dto.trip.TripCreateDTO;
import com.example.travel.planner.dto.trip.TripDTO;
import com.example.travel.planner.dto.trip.TripUpdateDTO;
import com.example.travel.planner.entity.Trip;
import com.example.travel.planner.entity.embeded.Activity;
import com.example.travel.planner.entity.embeded.Location;
import com.example.travel.planner.exception.ResourceNotFoundException;
import com.example.travel.planner.mapper.ActivityMapper;
import com.example.travel.planner.mapper.TripMapper;
import com.example.travel.planner.repository.TripRepository;
import com.example.travel.planner.service.GeoService;
import com.example.travel.planner.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;
    private final ActivityMapper activityMapper;
    private final GeoService geoService;

    @Override
    public Page<TripDTO> findAllTrips(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Trip> tripPage = tripRepository.findAll(pageable);

        return tripPage.map(tripMapper::toDto);
    }

    @Override
    @PreAuthorize("@tripSecurity.isOwner(#id, authentication)")
    public TripDTO findTripById(Authentication authentication, String id) {

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", id));

        return tripMapper.toDto(trip);
    }

    @Override
    public TripDTO createTrip(Authentication authentication, TripCreateDTO tripCreateDTO) {

        Trip trip = tripMapper.toEntity(tripCreateDTO, authentication);

        Trip savedTrip = tripRepository.save(trip);
        return tripMapper.toDto(savedTrip);
    }

    @Override
    @PreAuthorize("@tripSecurity.isOwner(#id, authentication)")
    public TripDTO updateTripById(Authentication authentication, String id, TripUpdateDTO tripUpdateDTO) {

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", id));

        tripMapper.updateEntityFromDto(tripUpdateDTO, trip);

        tripRepository.save(trip);

        return tripMapper.toDto(trip);
    }

    @Override
    @PreAuthorize("@tripSecurity.isOwner(#id, authentication)")
    public void deleteTripById(Authentication authentication, String id) {

        if (!tripRepository.existsById(id)) {
            throw new ResourceNotFoundException("Trip", id);
        }

        tripRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("@tripSecurity.isOwner(#tripId, authentication)")
    public TripDTO addActivityToTrip(Authentication authentication, String tripId, ActivityCreateDTO activityCreateDTO) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", tripId));

        if (trip.getActivities() == null) {
            trip.setActivities(new ArrayList<>());
        }

        Location location = geoService.searchLocation(activityCreateDTO.getLocationQuery());

        Activity activity = activityMapper.toEntity(activityCreateDTO, location);

        trip.getActivities().add(activity);

        Trip savedTrip = tripRepository.save(trip);

        return tripMapper.toDto(savedTrip);
    }

    @Override
    @PreAuthorize("@tripSecurity.isOwner(#tripId, authentication)")
    public void deleteActivityById(Authentication authentication, String tripId, String activityId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", tripId));

        List<Activity> activities = trip.getActivities();

        checkIfTripHasActivities(activities, activityId);

        boolean removed = activities.removeIf(activity -> activity.getId().equals(activityId));

        if (!removed) {
            throw new ResourceNotFoundException("Activity", activityId);
        }

        tripRepository.save(trip);

    }

    @Override
    @PreAuthorize("@tripSecurity.isOwner(#tripId, authentication)")
    public TripDTO updateActivityById(Authentication authentication, String tripId, String activityId,
                                      ActivityUpdateDTO activityUpdateDTO) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", tripId));

        List<Activity> activities = trip.getActivities();

        checkIfTripHasActivities(activities, activityId);

        Activity activityToUpdate = activities.stream()
                .filter(activity -> activity.getId().equals(activityId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Activity", activityId));

        activityMapper.updateEntityFromDto(activityUpdateDTO, activityToUpdate);

        Location newLocation = geoService.searchLocation(activityUpdateDTO.getLocationQuery());
        activityToUpdate.setLocation(newLocation);

        Trip savedTrip = tripRepository.save(trip);

        return tripMapper.toDto(savedTrip);
    }

    private void checkIfTripHasActivities(List<Activity> activities, String activityId) {
        Optional.ofNullable(activities)
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new ResourceNotFoundException("Activity", activityId));
    }

}
