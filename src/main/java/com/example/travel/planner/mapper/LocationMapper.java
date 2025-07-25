package com.example.travel.planner.mapper;

import com.example.travel.planner.dto.geoapify.GeoapifyResponse;
import com.example.travel.planner.dto.location.LocationDTO;
import com.example.travel.planner.entity.embeded.Location;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationMapper {
    LocationDTO toDto(Location location);

    Location fromGeoapify(GeoapifyResponse geoapifyResponse);
}
