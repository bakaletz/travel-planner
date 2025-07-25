package com.example.travel.planner.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class LoginResponseDTO {

    private String status;

    private String jwtToken;
}
