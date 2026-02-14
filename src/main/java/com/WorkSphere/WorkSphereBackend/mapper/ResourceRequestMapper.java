package com.WorkSphere.WorkSphereBackend.mapper;

import org.springframework.stereotype.Component;

import com.WorkSphere.WorkSphereBackend.dto.PendingRequestDto;
import com.WorkSphere.WorkSphereBackend.dto.ResourceRequestResponseDto;
import com.WorkSphere.WorkSphereBackend.entity.ResourceRequest;

@Component
public class ResourceRequestMapper {
	
	public ResourceRequestResponseDto toResourceRequestResponseDTO(ResourceRequest entity) {

        if (entity == null) {
            return null;
        }

        return ResourceRequestResponseDto.builder()
                .requestId(entity.getRequestId())
                .resourceCategoryId(
                        entity.getResource() != null 
                                ? entity.getResource().getResourceId() 
                                : null
                )
                .requestDate(entity.getRequestDate())
                .status(entity.getRequestStatus())
                .priority(entity.getPriorityLevel())
                .build();
    }
	
	
	public PendingRequestDto toPendingRequestDTO(ResourceRequest request) {

        return PendingRequestDto.builder()
                .requestId(request.getRequestId())
                .userName(request.getUser().getName())
                .resourceName(
                        request.getResource() != null
                                ? request.getResource().getResourceName()
                                : request.getCategory().getCategoryName()
                )
                .priorityLevel(request.getPriorityLevel())
                .requestDate(request.getRequestDate())
                .build();
    }
}
