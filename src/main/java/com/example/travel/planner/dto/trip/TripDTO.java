package com.example.travel.planner.dto.trip;

import com.example.travel.planner.dto.activity.ActivityDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class TripDTO {

    private String id;
    private String userId;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private List<ActivityDTO> activities;
}
