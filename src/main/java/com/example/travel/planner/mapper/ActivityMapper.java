package com.example.travel.planner.mapper;

import com.example.travel.planner.dto.activity.ActivityCreateDTO;
import com.example.travel.planner.dto.activity.ActivityDTO;
import com.example.travel.planner.entity.embeded.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActivityMapper {

    Activity toEntity(ActivityCreateDTO activityCreateDTO);

    ActivityDTO toDto (Activity activity);

    void updateEntityFromDto(ActivityCreateDTO activityCreateDTO, @MappingTarget Activity activity);
}
