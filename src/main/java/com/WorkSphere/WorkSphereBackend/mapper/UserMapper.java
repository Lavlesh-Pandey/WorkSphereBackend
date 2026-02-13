package com.WorkSphere.WorkSphereBackend.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.WorkSphere.WorkSphereBackend.dto.UserDetailsDto;
import com.WorkSphere.WorkSphereBackend.entity.Users;
@Component
public class UserMapper {

    // ENTITY → DTO
    public UserDetailsDto toUserDetailsDto(Users user) {

        if (user == null) {
            return null;
        }

        return UserDetailsDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .phone(user.getPhone())
                .build();
    }
    
    
}
