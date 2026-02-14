package com.WorkSphere.WorkSphereBackend.dto;

import java.time.LocalDateTime;

import com.WorkSphere.WorkSphereBackend.enums.PriorityLevel;
import com.WorkSphere.WorkSphereBackend.enums.RequestStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResourceRequestResponseDto {

    private Integer requestId;
    private Integer resourceCategoryId;
    private LocalDateTime requestDate;
    private RequestStatus status;
    private PriorityLevel priority;
}