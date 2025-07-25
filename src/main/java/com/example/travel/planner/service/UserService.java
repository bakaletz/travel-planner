package com.example.travel.planner.service;


import com.example.travel.planner.dto.user.UserDTO;
import com.example.travel.planner.dto.user.UserRegisterDTO;
import com.example.travel.planner.dto.user.UserUpdateDTO;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<UserDTO> findAllUsers(int page, int size);

    UserDTO findById(String id);

    UserDTO createUser(UserRegisterDTO userRegisterDTO);

    UserDTO updateUser(String id, UserUpdateDTO userUpdateDTO);

    void deleteUser(String id);
}
