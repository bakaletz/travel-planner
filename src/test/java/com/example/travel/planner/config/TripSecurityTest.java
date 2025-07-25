package com.example.travel.planner.config;

import com.example.travel.planner.entity.Trip;
import com.example.travel.planner.repository.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripSecurityTest {

    @Mock
    private TripRepository tripRepository;
    @InjectMocks
    private TripSecurity tripSecurity;
    @Mock
    private Authentication authentication;


    @Test
    void isOwner_ShouldReturnTrue_WhenUserIsOwner() {
        Trip trip = new Trip();
        trip.setUserId("user123");
        when(authentication.getName()).thenReturn("user123");
        when(tripRepository.findById("trip1")).thenReturn(Optional.of(trip));

        boolean result = tripSecurity.isOwner("trip1", authentication);

        assertTrue(result);

        verify(authentication).getName();
        verify(tripRepository).findById(any());
    }

    @Test
    void isOwner_ShouldReturnFalse_WhenUserIsNotOwner() {
        Trip trip = new Trip();
        trip.setUserId("user123");
        when(authentication.getName()).thenReturn("otherUser");
        when(tripRepository.findById("trip1")).thenReturn(Optional.of(trip));

        boolean result = tripSecurity.isOwner("trip1", authentication);

        assertFalse(result);

        verify(authentication).getName();
        verify(tripRepository).findById(any());
    }

    @Test
    void isOwner_ShouldThrowException_WhenTripNotFound() {
        when(tripRepository.findById("trip1")).thenReturn(Optional.empty());

        assertThrows(AccessDeniedException.class, () -> tripSecurity.isOwner("trip1", authentication));

        verify(tripRepository).findById(any());

    }
}
