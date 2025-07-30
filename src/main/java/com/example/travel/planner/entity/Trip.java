package com.example.travel.planner.entity;

import com.example.travel.planner.entity.embeded.Activity;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;


@Document(collection = "trips")
@Data
@Builder
public class Trip {

    @Id
    private String id;
    private String userId;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private List<Activity> activities;
}
