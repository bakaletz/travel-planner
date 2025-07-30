package com.example.travel.planner.mapper;

import com.example.travel.planner.dto.trip.TripCreateDTO;
import com.example.travel.planner.dto.trip.TripDTO;
import com.example.travel.planner.dto.trip.TripUpdateDTO;
import com.example.travel.planner.entity.Trip;
import com.example.travel.planner.testdata.TestTripProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripMapperTest {

    @Mock
    private Authentication authentication;

    private final TripMapper mapper = Mappers.getMapper(TripMapper.class);

    @Test
    void toDto_shouldMapFields() {
        Trip trip = TestTripProvider.createDefaultTrip();
        TripDTO dto = mapper.toDto(trip);

        assertEquals(trip.getTitle(), dto.getTitle());
    }

    @Test
    void toEntity_shouldMapFields() {
        TripCreateDTO createDTO = TestTripProvider.createTripCreateDTO();

        when(authentication.getName()).thenReturn("User");

        Trip entity = mapper.toEntity(createDTO, authentication);

        assertEquals(createDTO.getTitle(), entity.getTitle());
    }

    @Test
    void updateEntityFromDto_shouldUpdateFields() {
        TripUpdateDTO updateDTO = TestTripProvider.createTripUpdateDTO();
        Trip trip = TestTripProvider.createDefaultTrip();

        mapper.updateEntityFromDto(updateDTO, trip);

        assertEquals(trip.getTitle(), updateDTO.getTitle());
    }
}
