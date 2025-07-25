package com.example.travel.planner.mapper;


import com.example.travel.planner.dto.trip.TripCreateDTO;
import com.example.travel.planner.dto.trip.TripDTO;
import com.example.travel.planner.dto.trip.TripUpdateDTO;
import com.example.travel.planner.entity.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",uses = ActivityMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE )
public interface TripMapper {


    TripDTO toDto(Trip trip);

    Trip toEntity(TripCreateDTO tripCreateDTO);

    void updateEntityFromDto(TripUpdateDTO tripUpdateDTO,@MappingTarget Trip trip);
}
