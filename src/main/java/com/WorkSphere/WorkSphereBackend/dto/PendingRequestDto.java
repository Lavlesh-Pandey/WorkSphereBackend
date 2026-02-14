package com.WorkSphere.WorkSphereBackend.dto;

import java.time.LocalDateTime;

import com.WorkSphere.WorkSphereBackend.enums.PriorityLevel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PendingRequestDto {

    private Integer requestId;
    private String userName;
    private String resourceName;
    private PriorityLevel priorityLevel;
    private LocalDateTime requestDate;
}
