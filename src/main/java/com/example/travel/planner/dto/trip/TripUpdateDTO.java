package com.example.travel.planner.dto.trip;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@Builder
public class TripUpdateDTO {

    @NotBlank(message = "Title is required")
    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

}
