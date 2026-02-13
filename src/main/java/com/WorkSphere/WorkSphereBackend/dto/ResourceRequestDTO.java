package com.WorkSphere.WorkSphereBackend.dto;

import com.WorkSphere.WorkSphereBackend.enums.PriorityLevel;

import lombok.Data;

@Data
public class ResourceRequestDTO {

    private String userEmail;
    private String resourceName;
    private String description;
    private PriorityLevel priority;
}