package com.WorkSphere.WorkSphereBackend.entity;

import com.WorkSphere.WorkSphereBackend.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "USERS")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "name")
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone")
    private Long phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
}
