package com.WorkSphere.WorkSphereBackend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginRequestDto {
	private String email;
	private String password;
}