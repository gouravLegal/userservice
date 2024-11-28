package com.example.userservice.dtos;

import com.example.userservice.models.Role;
import com.example.userservice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponseDto {
    private String name;
    private String email;
    private List<Role> roles;

    public static UserResponseDto from(User user) {
        UserResponseDto responseDTO = new UserResponseDto();
        if (user != null) {
            responseDTO.setName(user.getName());
            responseDTO.setEmail(user.getEmail());
            responseDTO.setRoles(user.getRoles());
        }
        return responseDTO;
    }
}
