package com.WorkSphere.WorkSphereBackend.dto;

import java.time.LocalDateTime;

import com.WorkSphere.WorkSphereBackend.enums.PriorityLevel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResourceRequestResponseDTO {

    private Integer requestId;
    private Integer resourceId;
    private LocalDateTime requestDate;
    private String status;
    private PriorityLevel priority;
}