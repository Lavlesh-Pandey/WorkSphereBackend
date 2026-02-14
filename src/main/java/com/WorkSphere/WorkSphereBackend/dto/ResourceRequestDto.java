package com.WorkSphere.WorkSphereBackend.dto;

import com.WorkSphere.WorkSphereBackend.enums.PriorityLevel;

import lombok.Data;

@Data
public class ResourceRequestDto {

    private String userEmail;
    private String resourceCategoryName;
    private String description;
    private PriorityLevel priority;
}