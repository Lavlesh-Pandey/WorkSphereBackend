package com.WorkSphere.WorkSphereBackend.mapper;

import org.springframework.stereotype.Component;

import com.WorkSphere.WorkSphereBackend.dto.ResourceRequestResponseDTO;
import com.WorkSphere.WorkSphereBackend.entity.ResourceRequest;

@Component
public class ResourceRequestMapper {
	
	public ResourceRequestResponseDTO toResourceRequestResponseDTO(ResourceRequest entity) {

        if (entity == null) {
            return null;
        }

        return ResourceRequestResponseDTO.builder()
                .requestId(entity.getRequestId())
                .resourceId(
                        entity.getResource() != null 
                                ? entity.getResource().getResourceId() 
                                : null
                )
                .requestDate(entity.getRequestDate())
                .status(entity.getStatus())
                .priority(entity.getPriorityLevel())
                .build();
    }
}
