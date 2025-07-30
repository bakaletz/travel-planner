package com.example.travel.planner.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO {

    private String id;
    private String username;
    private String email;
    private String firstName;
}
