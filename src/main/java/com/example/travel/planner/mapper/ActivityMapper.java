package com.example.travel.planner.mapper;

import com.example.travel.planner.dto.activity.ActivityCreateDTO;
import com.example.travel.planner.dto.activity.ActivityDTO;
import com.example.travel.planner.dto.activity.ActivityUpdateDTO;
import com.example.travel.planner.entity.embeded.Activity;
import com.example.travel.planner.entity.embeded.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActivityMapper {
    @Mapping(target = "location", source = "location")
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    Activity toEntity(ActivityCreateDTO activityCreateDTO, Location location);

    ActivityDTO toDto(Activity activity);

    void updateEntityFromDto(ActivityUpdateDTO activityUpdateDTO, @MappingTarget Activity activity);
}
