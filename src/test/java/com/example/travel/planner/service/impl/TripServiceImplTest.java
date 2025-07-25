package com.example.travel.planner.service.impl;

import com.example.travel.planner.dto.activity.ActivityCreateDTO;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    private TripDTO testTripDTO;
    private TripCreateDTO testTripCreateDTO;
    private TripUpdateDTO testTripUpdateDTO;
    private Activity testActivity;
    private ActivityCreateDTO testActivityCreateDTO;
    private Location testLocation;

    @BeforeEach
    void setUp() {
        testTrip = new Trip();
        testTrip.setId("trip-123");
        testTrip.setUserId("user-123");
        testTrip.setTitle("Test Trip");

        testTripDTO = new TripDTO();
        testTripDTO.setId("trip-123");
        testTripDTO.setTitle("Test Trip");

        testTripCreateDTO = new TripCreateDTO();
        testTripCreateDTO.setTitle("New Trip");

        testTripUpdateDTO = new TripUpdateDTO();
        testTripUpdateDTO.setTitle("Updated Trip");

        testActivity = new Activity();
        testActivity.setId("activity-123");
        testActivity.setTitle("Test Activity");

        testActivityCreateDTO = new ActivityCreateDTO();
        testActivityCreateDTO.setTitle("New Activity");
        testActivityCreateDTO.setLocationQuery("Kyiv Ukraine");

        testLocation = new Location();
        testLocation.setCity("Kyiv");
        testLocation.setCountry("Ukraine");
    }

    @Test
    void findAllTrips_shouldReturnListOfTrips() {
        Trip trip1 = new Trip();
        trip1.setId("1");
        Trip trip2 = new Trip();
        trip2.setId("2");
        List<Trip> trips = List.of(trip1, trip2);

        TripDTO dto1 = new TripDTO();
        dto1.setId("1");
        TripDTO dto2 = new TripDTO();
        dto2.setId("2");

        when(tripRepository.findAll()).thenReturn(trips);
        when(tripMapper.toDto(trip1)).thenReturn(dto1);
        when(tripMapper.toDto(trip2)).thenReturn(dto2);

        List<TripDTO> result = tripService.findAllTrips();

        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("2", result.get(1).getId());
        verify(tripRepository).findAll();
        verify(tripMapper, times(2)).toDto(any(Trip.class));
    }

    @Test
    void findTripById_shouldReturnTripDTO() {
        String id = "trip-123";

        when(tripRepository.findById(id)).thenReturn(Optional.of(testTrip));
        when(tripMapper.toDto(testTrip)).thenReturn(testTripDTO);

        TripDTO result = tripService.findTripById(authentication, id);

        assertNotNull(result);
        assertEquals(testTripDTO.getId(), result.getId());
        verify(tripRepository).findById(id);
        verify(tripMapper).toDto(testTrip);
    }

    @Test
    void findTripById_shouldThrowResourceNotFoundException() {
        String id = "nonexistent-trip";

        when(tripRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tripService.findTripById(authentication, id));
        verify(tripRepository).findById(id);
        verifyNoInteractions(tripMapper);
    }

    @Test
    void createTrip_shouldSaveNewTrip() {
        String userId = "user-123";
        Trip newTrip = new Trip();
        newTrip.setTitle("New Trip");

        when(authentication.getName()).thenReturn(userId);
        when(tripMapper.toEntity(testTripCreateDTO)).thenReturn(newTrip);
        when(tripRepository.save(newTrip)).thenReturn(testTrip);
        when(tripMapper.toDto(testTrip)).thenReturn(testTripDTO);

        TripDTO result = tripService.createTrip(authentication, testTripCreateDTO);

        assertNotNull(result);
        assertEquals(testTripDTO.getId(), result.getId());
        assertEquals(userId, newTrip.getUserId());
        verify(authentication).getName();
        verify(tripMapper).toEntity(testTripCreateDTO);
        verify(tripRepository).save(newTrip);
        verify(tripMapper).toDto(testTrip);
    }

    @Test
    void updateTripById_shouldUpdateAndReturnDto_whenTripExists() {
        String tripId = "trip-123";

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));
        doNothing().when(tripMapper).updateEntityFromDto(testTripUpdateDTO, testTrip);
        when(tripRepository.save(testTrip)).thenReturn(testTrip);
        when(tripMapper.toDto(testTrip)).thenReturn(testTripDTO);

        TripDTO result = tripService.updateTripById(authentication, tripId, testTripUpdateDTO);

        assertNotNull(result);
        assertEquals(tripId, result.getId());
        verify(tripRepository).findById(tripId);
        verify(tripMapper).updateEntityFromDto(testTripUpdateDTO, testTrip);
        verify(tripRepository).save(testTrip);
        verify(tripMapper).toDto(testTrip);
    }

    @Test
    void updateTripById_shouldThrowResourceNotFoundException_whenTripNotFound() {
        String tripId = "nonexistent-trip";

        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.updateTripById(authentication, tripId, testTripUpdateDTO));
        verify(tripRepository).findById(tripId);
        verifyNoInteractions(tripMapper);
    }

    @Test
    void deleteTripById_shouldDeleteTripAndReturnMessage() {
        String tripId = "trip-123";

        when(tripRepository.existsById(tripId)).thenReturn(true);
        doNothing().when(tripRepository).deleteById(tripId);

        String result = tripService.deleteTripById(authentication, tripId);

        assertEquals("Trip with id: " + tripId + " was successfully deleted.", result);
        verify(tripRepository).existsById(tripId);
        verify(tripRepository).deleteById(tripId);
    }

    @Test
    void deleteTripById_shouldThrowResourceNotFoundException_whenTripNotExists() {
        String tripId = "nonexistent-trip";

        when(tripRepository.existsById(tripId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> tripService.deleteTripById(authentication, tripId));
        verify(tripRepository).existsById(tripId);
        verify(tripRepository, never()).deleteById(any());
    }

    @Test
    void addActivityToTrip_shouldAddActivityAndReturnTripDTO() {
        String tripId = "trip-123";
        testTrip.setActivities(new ArrayList<>());

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));
        when(geoService.searchLocation(testActivityCreateDTO.getLocationQuery())).thenReturn(testLocation);
        when(activityMapper.toEntity(testActivityCreateDTO)).thenReturn(testActivity);
        when(tripRepository.save(testTrip)).thenReturn(testTrip);
        when(tripMapper.toDto(testTrip)).thenReturn(testTripDTO);

        TripDTO result = tripService.addActivityToTrip(authentication, tripId, testActivityCreateDTO);

        assertNotNull(result);
        assertEquals(testTripDTO.getId(), result.getId());
        assertTrue(testTrip.getActivities().contains(testActivity));
        assertEquals(testLocation, testActivity.getLocation());
        assertNotNull(testActivity.getId());
        verify(tripRepository).findById(tripId);
        verify(geoService).searchLocation(testActivityCreateDTO.getLocationQuery());
        verify(activityMapper).toEntity(testActivityCreateDTO);
        verify(tripRepository).save(testTrip);
        verify(tripMapper).toDto(testTrip);
    }

    @Test
    void addActivityToTrip_shouldInitializeActivitiesIfNull() {
        String tripId = "trip-123";
        testTrip.setActivities(null);

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));
        when(geoService.searchLocation(testActivityCreateDTO.getLocationQuery())).thenReturn(testLocation);
        when(activityMapper.toEntity(testActivityCreateDTO)).thenReturn(testActivity);
        when(tripRepository.save(testTrip)).thenReturn(testTrip);
        when(tripMapper.toDto(testTrip)).thenReturn(testTripDTO);

        TripDTO result = tripService.addActivityToTrip(authentication, tripId, testActivityCreateDTO);

        assertNotNull(result);
        assertNotNull(testTrip.getActivities());
        assertTrue(testTrip.getActivities().contains(testActivity));
    }

    @Test
    void addActivityToTrip_shouldThrowResourceNotFoundException_whenTripNotFound() {
        String tripId = "nonexistent-trip";

        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.addActivityToTrip(authentication, tripId, testActivityCreateDTO));
        verify(tripRepository).findById(tripId);
        verifyNoInteractions(geoService);
        verifyNoInteractions(activityMapper);
    }

    @Test
    void deleteActivityById_shouldDeleteActivityAndReturnMessage() {
        String tripId = "trip-123";
        String activityId = "activity-123";
        testActivity.setId(activityId);
        testTrip.setActivities(new ArrayList<>(List.of(testActivity)));

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));
        when(tripRepository.save(testTrip)).thenReturn(testTrip);

        String result = tripService.deleteActivityById(authentication, tripId, activityId);

        assertEquals("Activity with id " + activityId + " was successfully deleted from trip " + tripId, result);
        assertNull(testTrip.getActivities());
        verify(tripRepository).findById(tripId);
        verify(tripRepository).save(testTrip);
    }

    @Test
    void deleteActivityById_shouldKeepActivitiesWhenNotEmpty() {
        String tripId = "trip-123";
        String activityId = "activity-123";
        testActivity.setId(activityId);

        Activity anotherActivity = new Activity();
        anotherActivity.setId("activity-456");

        testTrip.setActivities(new ArrayList<>(List.of(testActivity, anotherActivity)));

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));
        when(tripRepository.save(testTrip)).thenReturn(testTrip);

        String result = tripService.deleteActivityById(authentication, tripId, activityId);

        assertEquals("Activity with id " + activityId + " was successfully deleted from trip " + tripId, result);
        assertNotNull(testTrip.getActivities());
        assertEquals(1, testTrip.getActivities().size());
        assertEquals("activity-456", testTrip.getActivities().get(0).getId());
        verify(tripRepository).findById(tripId);
        verify(tripRepository).save(testTrip);
    }

    @Test
    void deleteActivityById_shouldThrowResourceNotFoundException_whenTripNotFound() {
        String tripId = "nonexistent-trip";
        String activityId = "activity-123";

        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.deleteActivityById(authentication, tripId, activityId));
        verify(tripRepository).findById(tripId);
        verifyNoMoreInteractions(tripRepository);
    }

    @Test
    void deleteActivityById_shouldThrowResourceNotFoundException_whenActivitiesNull() {
        String tripId = "trip-123";
        String activityId = "activity-123";
        testTrip.setActivities(null);

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.deleteActivityById(authentication, tripId, activityId));
        verify(tripRepository).findById(tripId);
        verifyNoMoreInteractions(tripRepository);
    }

    @Test
    void deleteActivityById_shouldThrowResourceNotFoundException_whenActivitiesEmpty() {
        String tripId = "trip-123";
        String activityId = "activity-123";
        testTrip.setActivities(new ArrayList<>());

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.deleteActivityById(authentication, tripId, activityId));
        verify(tripRepository).findById(tripId);
        verifyNoMoreInteractions(tripRepository);
    }

    @Test
    void deleteActivityById_shouldThrowResourceNotFoundException_whenActivityNotFound() {
        String tripId = "trip-123";
        String activityId = "nonexistent-activity";
        Activity otherActivity = new Activity();
        otherActivity.setId("different-activity");
        testTrip.setActivities(new ArrayList<>(List.of(otherActivity)));

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.deleteActivityById(authentication, tripId, activityId));
        verify(tripRepository).findById(tripId);
        verifyNoMoreInteractions(tripRepository);
    }

    @Test
    void updateActivityById_shouldUpdateActivityAndReturnTripDTO() {
        String tripId = "trip-123";
        String activityId = "activity-123";
        testActivity.setId(activityId);
        testTrip.setActivities(new ArrayList<>(List.of(testActivity)));

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));
        doNothing().when(activityMapper).updateEntityFromDto(testActivityCreateDTO, testActivity);
        when(geoService.searchLocation(testActivityCreateDTO.getLocationQuery())).thenReturn(testLocation);
        when(tripRepository.save(testTrip)).thenReturn(testTrip);
        when(tripMapper.toDto(testTrip)).thenReturn(testTripDTO);

        TripDTO result = tripService.updateActivityById(authentication, tripId, activityId, testActivityCreateDTO);

        assertNotNull(result);
        assertEquals(testTripDTO.getId(), result.getId());
        assertEquals(testLocation, testActivity.getLocation());
        verify(tripRepository).findById(tripId);
        verify(activityMapper).updateEntityFromDto(testActivityCreateDTO, testActivity);
        verify(geoService).searchLocation(testActivityCreateDTO.getLocationQuery());
        verify(tripRepository).save(testTrip);
        verify(tripMapper).toDto(testTrip);
    }

    @Test
    void updateActivityById_shouldThrowResourceNotFoundException_whenTripNotFound() {
        String tripId = "nonexistent-trip";
        String activityId = "activity-123";

        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.updateActivityById(authentication, tripId, activityId, testActivityCreateDTO));
        verify(tripRepository).findById(tripId);
        verifyNoInteractions(activityMapper);
        verifyNoInteractions(geoService);
    }

    @Test
    void updateActivityById_shouldThrowResourceNotFoundException_whenActivitiesNull() {
        String tripId = "trip-123";
        String activityId = "activity-123";
        testTrip.setActivities(null);

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.updateActivityById(authentication, tripId, activityId, testActivityCreateDTO));
        verify(tripRepository).findById(tripId);
        verifyNoInteractions(activityMapper);
        verifyNoInteractions(geoService);
    }

    @Test
    void updateActivityById_shouldThrowResourceNotFoundException_whenActivitiesEmpty() {
        String tripId = "trip-123";
        String activityId = "activity-123";
        testTrip.setActivities(new ArrayList<>());

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.updateActivityById(authentication, tripId, activityId, testActivityCreateDTO));
        verify(tripRepository).findById(tripId);
        verifyNoInteractions(activityMapper);
        verifyNoInteractions(geoService);
    }

    @Test
    void updateActivityById_shouldThrowResourceNotFoundException_whenActivityNotFound() {
        String tripId = "trip-123";
        String activityId = "nonexistent-activity";
        Activity otherActivity = new Activity();
        otherActivity.setId("different-activity");
        testTrip.setActivities(new ArrayList<>(List.of(otherActivity)));

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));

        assertThrows(ResourceNotFoundException.class,
                () -> tripService.updateActivityById(authentication, tripId, activityId, testActivityCreateDTO));
        verify(tripRepository).findById(tripId);
        verifyNoInteractions(activityMapper);
        verifyNoInteractions(geoService);
    }
}