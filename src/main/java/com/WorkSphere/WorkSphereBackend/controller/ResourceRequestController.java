package com.WorkSphere.WorkSphereBackend.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.WorkSphere.WorkSphereBackend.dto.ResourceRequestDTO;
import com.WorkSphere.WorkSphereBackend.dto.ResourceRequestResponseDTO;
import com.WorkSphere.WorkSphereBackend.entity.ResourceRequest;
import com.WorkSphere.WorkSphereBackend.enums.PriorityLevel;
import com.WorkSphere.WorkSphereBackend.service.ResourceRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/resourceRequest")
@RequiredArgsConstructor
public class ResourceRequestController {

    private final ResourceRequestService resourceRequestService;

    // =====================================================
    // 1️⃣ CREATE REQUEST
    // =====================================================
    @PostMapping("/create")
    public ResourceRequestResponseDTO createRequest( @RequestBody ResourceRequestDTO request) {

        return resourceRequestService.createResourceRequest(
                request.getUserEmail(),
                request.getResourceName(),
                request.getDescription(),
                request.getPriority()
        );
    }

    // =====================================================
    // 2️⃣ ACCEPT REQUEST
    // =====================================================
    @PutMapping("/accept/{requestId}")
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
}