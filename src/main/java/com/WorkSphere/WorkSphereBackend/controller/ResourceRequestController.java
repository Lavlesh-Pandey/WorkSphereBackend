package com.WorkSphere.WorkSphereBackend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.WorkSphere.WorkSphereBackend.dto.ResourceRequestDto;
import com.WorkSphere.WorkSphereBackend.dto.ResourceRequestResponseDto;
import com.WorkSphere.WorkSphereBackend.dto.PendingRequestDto;
import com.WorkSphere.WorkSphereBackend.service.ResourceRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/resource-request")
@RequiredArgsConstructor
public class ResourceRequestController {

    private final ResourceRequestService resourceRequestService;

    // =====================================================
    // 1️⃣ CREATE REQUEST
    // =====================================================
    @PostMapping("/create")
    public ResourceRequestResponseDto createRequest( @RequestBody ResourceRequestDto request) {

        return resourceRequestService.createResourceRequest(
                request.getUserEmail(),
                request.getResourceCategoryName(),
                request.getDescription(),
                request.getPriority()
        );
    }

    // =====================================================
    // 2️⃣ ACCEPT REQUEST
    // =====================================================
    @PutMapping("/approve/{requestId}")
    public String acceptRequest(@PathVariable Integer requestId) {

        resourceRequestService.acceptRequest(requestId);
        return "Request accepted successfully";
    }

    // =====================================================
    // 3️⃣ ALLOT RESOURCE
    // =====================================================
    @PutMapping("/allot/{requestId}")
    public String allotResource(@PathVariable Integer requestId) {

        resourceRequestService.allotResource(requestId);
        return "Resource allotted successfully";
    }

    // =====================================================
    // 4️⃣ RETURN RESOURCE
    // =====================================================
    @PutMapping("/return/{requestId}")
    public String returnResource(@PathVariable Integer requestId) {

        resourceRequestService.returnResource(requestId);
        return "Resource returned successfully";
    }
    
    @GetMapping("/pending")
    public List<PendingRequestDto> getPendingRequests() {

        return resourceRequestService.getAllPendingRequests();
    }
}