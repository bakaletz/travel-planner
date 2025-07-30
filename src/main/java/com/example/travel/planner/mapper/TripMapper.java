package com.example.travel.planner.mapper;


import com.example.travel.planner.dto.trip.TripCreateDTO;
import com.example.travel.planner.dto.trip.TripDTO;
import com.example.travel.planner.dto.trip.TripUpdateDTO;
import com.example.travel.planner.entity.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.Authentication;

@Mapper(componentModel = "spring", uses = ActivityMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TripMapper {
    TripDTO toDto(Trip trip);

    @Mapping(target = "userId", expression = "java(authentication.getName())")
    Trip toEntity(TripCreateDTO tripCreateDTO, Authentication authentication);

    void updateEntityFromDto(TripUpdateDTO tripUpdateDTO, @MappingTarget Trip trip);
}
