package com.example.travel.planner.dto.activity;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ActivityCreateDTO {

    @NotBlank(message = "Type is required")
    private String type;
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    private LocalDate timeFrom;
    private LocalDate timeTo;
    @NotBlank(message = "Location Query is required")
    private String locationQuery;

}
