package com.example.travel.planner.mapper;

import com.example.travel.planner.dto.location.LocationDTO;
import com.example.travel.planner.entity.embeded.Location;
import com.fasterxml.jackson.databind.JsonNode;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationMapper {

    LocationDTO toDto(Location location);

    default Location fromGeoapify(JsonNode props) {

        return Location.builder()
                .city(props.path("city").asText(""))
                .country(props.path("country").asText(""))
                .address(props.path("formatted").asText(""))
                .latitude(props.path("lat").asDouble())
                .longitude(props.path("lon").asDouble())
                .placeId(props.path("place_id").asText())
                .build();

    }
}
