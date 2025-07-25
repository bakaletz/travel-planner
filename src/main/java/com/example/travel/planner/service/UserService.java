package com.example.travel.planner.service;


import com.example.travel.planner.dto.user.UserDTO;
import com.example.travel.planner.dto.user.UserRegisterDTO;
import com.example.travel.planner.dto.user.UserUpdateDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll();

    UserDTO createUser(UserRegisterDTO userRegisterDTO);

    UserDTO updateUser(String id, UserUpdateDTO userUpdateDTO);

    String deleteUser(String id);

    UserDTO findById(String id);
}
