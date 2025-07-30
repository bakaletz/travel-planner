package com.example.travel.planner.service.impl;

import com.example.travel.planner.dto.activity.ActivityCreateDTO;
import com.example.travel.planner.dto.activity.ActivityUpdateDTO;
import com.example.travel.planner.dto.trip.TripCreateDTO;
import com.example.travel.planner.dto.trip.TripUpdateDTO;
import com.example.travel.planner.entity.Trip;
import com.example.travel.planner.entity.embeded.Activity;
import com.example.travel.planner.entity.embeded.Location;
import com.example.travel.planner.exception.ResourceNotFoundException;
import com.example.travel.planner.mapper.ActivityMapper;
import com.example.travel.planner.mapper.TripMapper;
import com.example.travel.planner.repository.TripRepository;
import com.example.travel.planner.service.GeoService;
import com.example.travel.planner.testdata.TestActivityProvider;
import com.example.travel.planner.testdata.TestTripProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripServiceImplTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private TripMapper tripMapper;

    @Mock
    private ActivityMapper activityMapper;

    @Mock
    private GeoService geoService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TripServiceImpl tripService;

    private Trip testTrip;
    private TripCreateDTO testTripCreateDTO;
    private TripUpdateDTO testTripUpdateDTO;
    private Activity testActivity;
    private ActivityCreateDTO testActivityCreateDTO;
    private ActivityUpdateDTO testActivityUpdateDTO;
    private Location testLocation;

    private final String tripId = TestTripProvider.TRIP_ID;
    private String activityId = TestActivityProvider.ACTIVITY_ID;

    @BeforeEach
    void setUp() {
        testTrip = TestTripProvider.createDefaultTrip();
        testTripCreateDTO = TestTripProvider.createTripCreateDTO();
        testTripUpdateDTO = TestTripProvider.createTripUpdateDTO();
        testLocation = Location.builder()
                .city("Kyiv")
                .country("Ukraine")
                .build();

        testActivity = TestActivityProvider.createDefaultActivity();
        testActivityCreateDTO = TestActivityProvider.createActivityCreateDTO();
        testActivityUpdateDTO = TestActivityProvider.createActivityUpdateDTO();

    }

    @Test
    void findAllTrips_shouldReturnListOfTrips() {
        Trip trip1 = Trip.builder()
                .id("1")
                .build();
        Trip trip2 = Trip.builder()
                .id("2")
                .build();

        List<Trip> trips = List.of(trip1, trip2);

        Page<Trip> tripPage = new PageImpl<>(trips);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        when(tripRepository.findAll(pageable)).thenReturn(tripPage);

        tripService.findAllTrips(0, 10);

        verify(tripMapper, times(2)).toDto(any());
    }

    @Test
    void findTripById_shouldReturnTripDTO() {
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));

        tripService.findTripById(authentication, tripId);

        verify(tripMapper).toDto(eq(testTrip));
    }

    @Test
    void findTripById_shouldThrowResourceNotFoundException() {
        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tripService.findTripById(authentication, tripId));

        verifyNoInteractions(tripMapper);
    }

    @Test
    void createTrip_shouldSaveNewTrip() {

        tripService.createTrip(authentication, testTripCreateDTO);

        verify(tripMapper).toEntity(testTripCreateDTO, authentication);
        verify(tripMapper).toDto(any());
        verify(tripRepository).save(any());

    }

    @Test
    void updateTripById_shouldUpdateAndReturnDto_whenTripExists() {
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));

        tripService.updateTripById(authentication, tripId, testTripUpdateDTO);

        verify(tripMapper).updateEntityFromDto(testTripUpdateDTO, testTrip);
        verify(tripRepository).save(testTrip);
        verify(tripMapper).toDto(any());
    }

    @Test
    void updateTripById_shouldThrowResourceNotFoundException_whenTripNotFound() {
        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.updateTripById(authentication, tripId, testTripUpdateDTO));

        verifyNoInteractions(tripMapper);
    }

    @Test
    void deleteTripById_shouldDeleteTrip() {
        when(tripRepository.existsById(tripId)).thenReturn(true);

        tripService.deleteTripById(authentication, tripId);

        verify(tripRepository).deleteById(tripId);
    }

    @Test
    void deleteTripById_shouldThrowResourceNotFoundException_whenTripNotExists() {
        when(tripRepository.existsById(tripId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> tripService.deleteTripById(authentication, tripId));

        verify(tripRepository, never()).deleteById(any());
    }

    @Test
    void addActivityToTrip_shouldAddActivityAndReturnTripDTO() {
        testTrip.setActivities(new ArrayList<>());

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));

        tripService.addActivityToTrip(authentication, tripId, testActivityCreateDTO);

        verify(activityMapper).toEntity(eq(testActivityCreateDTO), any());
        verify(tripMapper).toDto(any());
        verify(geoService).searchLocation(testActivityCreateDTO.getLocationQuery());
        verify(tripRepository).save(testTrip);

    }

    @Test
    void addActivityToTrip_shouldInitializeActivitiesIfNull() {
        testTrip.setActivities(null);

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));

        tripService.addActivityToTrip(authentication, tripId, testActivityCreateDTO);

        assertNotNull(testTrip.getActivities());

        verify(activityMapper).toEntity(eq(testActivityCreateDTO), any());
        verify(tripMapper).toDto(any());
        verify(geoService).searchLocation(testActivityCreateDTO.getLocationQuery());
        verify(tripRepository).save(testTrip);
    }

    @Test
    void addActivityToTrip_shouldThrowResourceNotFoundException_whenTripNotFound() {
        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.addActivityToTrip(authentication, tripId, testActivityCreateDTO));

        verifyNoInteractions(geoService);
        verifyNoInteractions(activityMapper);
    }

    @Test
    void deleteActivityById_shouldDeleteActivity() {
        testTrip.setActivities(new ArrayList<>(List.of(testActivity)));

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));

        tripService.deleteActivityById(authentication, tripId, activityId);

        assertTrue(testTrip.getActivities().isEmpty());

        verify(tripRepository).save(testTrip);
    }

    @Test
    void deleteActivityById_shouldKeepActivitiesWhenNotEmpty() {
        Activity anotherActivity = Activity.builder()
                .id("activity-456")
                .build();
        testTrip.setActivities(new ArrayList<>(List.of(testActivity, anotherActivity)));

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));

        tripService.deleteActivityById(authentication, tripId, activityId);

        assertEquals(anotherActivity, testTrip.getActivities().getFirst());

        verify(tripRepository).save(testTrip);
    }

    @Test
    void deleteActivityById_shouldThrowResourceNotFoundException_whenTripNotFound() {
        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.deleteActivityById(authentication, tripId, activityId));

        verifyNoMoreInteractions(tripRepository);
    }

    @Test
    void deleteActivityById_shouldThrowResourceNotFoundException_whenActivitiesNull() {
        testTrip.setActivities(null);

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.deleteActivityById(authentication, tripId, activityId));

        verifyNoMoreInteractions(tripRepository);
    }

    @Test
    void deleteActivityById_shouldThrowResourceNotFoundException_whenActivitiesEmpty() {
        testTrip.setActivities(new ArrayList<>());

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.deleteActivityById(authentication, tripId, activityId));

        verifyNoMoreInteractions(tripRepository);
    }

    @Test
    void deleteActivityById_shouldThrowResourceNotFoundException_whenActivityNotFound() {
        Activity otherActivity = Activity.builder()
                .id("different-activity")
                .build();
        testTrip.setActivities(new ArrayList<>(List.of(otherActivity)));

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.deleteActivityById(authentication, tripId, activityId));

        verifyNoMoreInteractions(tripRepository);
    }

    @Test
    void updateActivityById_shouldUpdateActivityAndReturnTripDTO() {
        testTrip.setActivities(new ArrayList<>(List.of(testActivity)));

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));
        when(geoService.searchLocation(any())).thenReturn(testLocation);

        tripService.updateActivityById(authentication, tripId, activityId, testActivityUpdateDTO);

        assertEquals(testLocation, testActivity.getLocation());

        verify(activityMapper).updateEntityFromDto(testActivityUpdateDTO, testActivity);
        verify(tripRepository).save(testTrip);
        verify(tripMapper).toDto(any());
    }

    @Test
    void updateActivityById_shouldThrowResourceNotFoundException_whenTripNotFound() {
        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.updateActivityById(authentication, tripId, activityId, testActivityUpdateDTO));

        verify(tripRepository).findById(tripId);
        verifyNoInteractions(activityMapper);
        verifyNoInteractions(geoService);
    }

    @Test
    void updateActivityById_shouldThrowResourceNotFoundException_whenActivitiesNull() {
        testTrip.setActivities(null);

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.updateActivityById(authentication, tripId, activityId, testActivityUpdateDTO));

        verifyNoInteractions(activityMapper);
        verifyNoInteractions(geoService);
    }

    @Test
    void updateActivityById_shouldThrowResourceNotFoundException_whenActivitiesEmpty() {
        testTrip.setActivities(new ArrayList<>());

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.updateActivityById(authentication, tripId, activityId, testActivityUpdateDTO));

        verifyNoInteractions(activityMapper);
        verifyNoInteractions(geoService);
    }

    @Test
    void updateActivityById_shouldThrowResourceNotFoundException_whenActivityNotFound() {
        Activity otherActivity = Activity.builder()
                .id("different-activity")
                .build();
        testTrip.setActivities(new ArrayList<>(List.of(otherActivity)));

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.updateActivityById(authentication, tripId, activityId, testActivityUpdateDTO));

        verifyNoInteractions(activityMapper);
        verifyNoInteractions(geoService);
    }
}