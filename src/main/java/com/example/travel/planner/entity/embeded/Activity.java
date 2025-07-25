package com.example.travel.planner.entity.embeded;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Activity {

    private String id;

    private String type;

    private String title;

    private String description;

    private LocalDate timeFrom;

    private LocalDate timeTo;

    private Location location;

}
