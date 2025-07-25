package com.example.travel.planner.mapper;

import com.example.travel.planner.dto.trip.TripCreateDTO;
import com.example.travel.planner.dto.trip.TripDTO;
import com.example.travel.planner.dto.trip.TripUpdateDTO;
import com.example.travel.planner.entity.Trip;
import com.example.travel.planner.mapper.TripMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class TripMapperTest {

    private final TripMapper mapper = Mappers.getMapper(TripMapper.class);

    @Test
    void toDto_shouldMapFields() {
        Trip trip = new Trip();
        trip.setId("trip1");
        trip.setTitle("Vacation");
        trip.setDescription("Summer vacation");

        TripDTO dto = mapper.toDto(trip);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo("trip1");
        assertThat(dto.getTitle()).isEqualTo("Vacation");
        assertThat(dto.getDescription()).isEqualTo("Summer vacation");
    }

    @Test
    void toEntity_shouldMapFields() {
        TripCreateDTO createDTO = new TripCreateDTO();
        createDTO.setTitle("Trip to Paris");
        createDTO.setDescription("Visit museums");

        Trip entity = mapper.toEntity(createDTO);

        assertThat(entity).isNotNull();
        assertThat(entity.getTitle()).isEqualTo("Trip to Paris");
        assertThat(entity.getDescription()).isEqualTo("Visit museums");
    }

    @Test
    void updateEntityFromDto_shouldUpdateFields() {
        TripUpdateDTO updateDTO = new TripUpdateDTO();
        updateDTO.setTitle("Updated name");
        updateDTO.setDescription("Updated description");

        Trip trip = new Trip();
        trip.setTitle("Old name");
        trip.setDescription("Old description");

        mapper.updateEntityFromDto(updateDTO, trip);

        assertThat(trip.getTitle()).isEqualTo("Updated name");
        assertThat(trip.getDescription()).isEqualTo("Updated description");
    }
}
