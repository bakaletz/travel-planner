package com.example.travel.planner.testdata;

import com.example.travel.planner.dto.user.UserDTO;
import com.example.travel.planner.dto.user.UserRegisterDTO;
import com.example.travel.planner.dto.user.UserUpdateDTO;
import com.example.travel.planner.entity.User;

public class TestUserProvider {

    public static final String USER_ID = "user-123";

    public static User createDefaultUser() {
        return User.builder()
                .id(USER_ID)
                .email("test@example.com")
                .password("encrypted-123")
                .firstName("John")
                .build();
    }

    public static UserDTO createUserDTO() {
        return UserDTO.builder()
                .id(USER_ID)
                .email("test@example.com")
                .firstName("John")
                .build();
    }

    public static UserRegisterDTO createUserRegisterDTO() {
        return UserRegisterDTO.builder()
                .email("test@example.com")
                .firstName("John")
                .password("password123")
                .build();
    }

    public static UserUpdateDTO createUserUpdateDTO() {
        return UserUpdateDTO.builder()
                .email("test@example.com")
                .firstName("Updated")
                .password("password123")
                .build();
    }
}