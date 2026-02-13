package com.WorkSphere.WorkSphereBackend.dto;

import com.WorkSphere.WorkSphereBackend.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
	String jwt;
}