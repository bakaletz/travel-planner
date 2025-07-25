package com.example.travel.planner.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequestDTO {

    @Email(message = "Invalid email format")
    private String email;
    @Size(min = 6, message = "Invalid Password format")
    private String password;
}
