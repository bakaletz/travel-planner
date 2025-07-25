package com.example.travel.planner.controller;

import com.example.travel.planner.dto.user.UserRegisterDTO;
import com.example.travel.planner.dto.user.UserUpdateDTO;
import com.example.travel.planner.service.UserService;
import com.example.travel.planner.testdata.TestUserProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserRegisterDTO testUserRegisterDTO;
    private UserUpdateDTO testUserUpdateDTO;

    private final String userId = TestUserProvider.USER_ID;

    @BeforeEach
    void setUp() {
        testUserRegisterDTO = TestUserProvider.createUserRegisterDTO();
        testUserUpdateDTO = TestUserProvider.createUserUpdateDTO();
    }

    @Test
    void getUsers_ShouldVerify() {
        userController.getUsers(0, 10);

        verify(userService).findAllUsers(0, 10);

    }

    @Test
    void getUserById_ShouldVerify() {
        userController.getUserById(userId);

        verify(userService).findById(userId);
    }

    @Test
    void createUser_ShouldVerify() {
        userController.createUser(testUserRegisterDTO);

        verify(userService).createUser(testUserRegisterDTO);
    }

    @Test
    void updateUser_ShouldVerify() {
        userController.updateUser(userId, testUserUpdateDTO);

        verify(userService).updateUser(userId, testUserUpdateDTO);
    }

    @Test
    void deleteUser_ShouldVerify() {
        userController.deleteUser(userId);

        verify(userService).deleteUser(userId);
    }
}