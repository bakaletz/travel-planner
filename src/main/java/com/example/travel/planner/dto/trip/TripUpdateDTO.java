package com.example.travel.planner.dto.trip;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.time.LocalDate;

@Getter @Setter
public class TripUpdateDTO {

    @NotBlank(message = "Title is required")
    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

}
