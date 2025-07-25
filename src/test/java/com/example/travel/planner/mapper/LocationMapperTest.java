package com.example.travel.planner.mapper;

import com.example.travel.planner.dto.location.LocationDTO;
import com.example.travel.planner.entity.embeded.Location;
import com.example.travel.planner.mapper.LocationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class LocationMapperTest {

    private final LocationMapper mapper = Mappers.getMapper(LocationMapper.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void fromGeoapify_shouldMapAllFields() {
        ObjectNode props = objectMapper.createObjectNode();
        props.put("city", "Kyiv");
        props.put("country", "Ukraine");
        props.put("formatted", "Some address");
        props.put("lat", 50.45);
        props.put("lon", 30.52);
        props.put("place_id", "12345");

        Location location = mapper.fromGeoapify(props);

        assertThat(location).isNotNull();
        assertThat(location.getCity()).isEqualTo("Kyiv");
        assertThat(location.getCountry()).isEqualTo("Ukraine");
        assertThat(location.getAddress()).isEqualTo("Some address");
        assertThat(location.getLatitude()).isEqualTo(50.45);
        assertThat(location.getLongitude()).isEqualTo(30.52);
        assertThat(location.getPlaceId()).isEqualTo("12345");
    }

    @Test
    void toDto_shouldMapFields() {
        Location location = Location.builder()
                .city("Lviv")
                .country("Ukraine")
                .address("Main St")
                .latitude(49.84)
                .longitude(24.03)
                .placeId("abc123")
                .build();

        LocationDTO dto = mapper.toDto(location);

        assertThat(dto).isNotNull();
        assertThat(dto.getCity()).isEqualTo("Lviv");
        assertThat(dto.getCountry()).isEqualTo("Ukraine");
        assertThat(dto.getAddress()).isEqualTo("Main St");
        assertThat(dto.getLatitude()).isEqualTo(49.84);
        assertThat(dto.getLongitude()).isEqualTo(24.03);
        assertThat(dto.getPlaceId()).isEqualTo("abc123");
    }
}

