package com.WorkSphere.WorkSphereBackend.dto;

import com.WorkSphere.WorkSphereBackend.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsdto {

    private Integer userId;
    private String name;
    private String email;
    private Role role;
}
