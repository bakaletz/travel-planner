package com.example.travel.planner.service;

import com.example.travel.planner.dto.authentication.LoginRequestDTO;
import com.example.travel.planner.dto.authentication.LoginResponseDTO;
import com.example.travel.planner.dto.user.UserDTO;
import com.example.travel.planner.dto.user.UserRegisterDTO;
import jakarta.validation.Valid;

public interface AuthService {
    LoginResponseDTO authenticateUser(LoginRequestDTO loginRequest);

    UserDTO registerUser(@Valid UserRegisterDTO userDTO);
}
