package com.example.travel.planner.config;

import com.example.travel.planner.entity.Trip;
import com.example.travel.planner.exception.ResourceNotFoundException;
import com.example.travel.planner.repository.TripRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripSecurityTest {

    @Mock
    private TripRepository tripRepository;
    @InjectMocks
    private TripSecurity tripSecurity;
    @Mock
    private Authentication authentication;

    @Test
    void isOwner_ShouldVerify_WhenUserIsOwner() {
        Trip trip = Trip.builder()
                .userId("user123")
                .build();
        when(authentication.getName()).thenReturn("user123");
        when(tripRepository.findById("trip1")).thenReturn(Optional.of(trip));

        tripSecurity.isOwner("trip1", authentication);

        verify(authentication).getName();
    }

    @Test
    void isOwner_ShouldNotVerify_WhenUserIsNotOwner() {
        Trip trip = Trip.builder()
                .userId("user123")
                .build();

        when(tripRepository.findById("trip1")).thenReturn(Optional.of(trip));

        tripSecurity.isOwner("trip1", authentication);

        verify(authentication).getName();
    }

    @Test
    void isOwner_ShouldThrowException_WhenTripNotFound() {
        when(tripRepository.findById("trip1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tripSecurity.isOwner("trip1", authentication));
    }
}
