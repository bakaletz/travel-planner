package com.example.travel.planner.mapper;

import com.example.travel.planner.dto.geoapify.GeoapifyResponse;
import com.example.travel.planner.dto.location.LocationDTO;
import com.example.travel.planner.entity.embeded.Location;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocationMapperTest {

    private final LocationMapper locationMapper = Mappers.getMapper(LocationMapper.class);

    @Test
    void fromGeoapify_shouldMapAllFields() {
        GeoapifyResponse geoapifyResponse = GeoapifyResponse.builder()
                .city("Ternopil")
                .country("Ukraine")
                .build();

        Location location = locationMapper.fromGeoapify(geoapifyResponse);

        assertEquals(geoapifyResponse.getCity(), location.getCity());
        assertEquals(geoapifyResponse.getCountry(), location.getCountry());
    }

    @Test
    void toDto_shouldMapFields() {
        Location location = Location.builder()
                .city("Ternopil")
                .country("Ukraine")
                .build();

        LocationDTO dto = locationMapper.toDto(location);

        assertEquals(location.getCity(), dto.getCity());
        assertEquals(location.getCountry(), dto.getCountry());
    }
}

