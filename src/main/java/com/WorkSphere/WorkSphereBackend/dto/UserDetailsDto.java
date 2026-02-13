package com.WorkSphere.WorkSphereBackend.dto;

import com.WorkSphere.WorkSphereBackend.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsDto {

    private Integer userId;
    private String name;
    private String email;
    private String phone;
    private Role role;
}
