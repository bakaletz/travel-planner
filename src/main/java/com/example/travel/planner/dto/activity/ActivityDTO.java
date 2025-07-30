package com.example.travel.planner.dto.activity;

import com.example.travel.planner.dto.location.LocationDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ActivityDTO {

    private String id;
    private String type;
    private String title;
    private String description;
    private LocalDate timeFrom;
    private LocalDate timeTo;
    private LocationDTO location;

}
