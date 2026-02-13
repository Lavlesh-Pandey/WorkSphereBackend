package com.WorkSphere.WorkSphereBackend.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.WorkSphere.WorkSphereBackend.dto.ResourceRequestResponseDTO;
import com.WorkSphere.WorkSphereBackend.entity.Notification;
import com.WorkSphere.WorkSphereBackend.entity.ResourceAllocationHistory;
import com.WorkSphere.WorkSphereBackend.entity.ResourceCategory;
import com.WorkSphere.WorkSphereBackend.entity.ResourceRequest;
import com.WorkSphere.WorkSphereBackend.entity.Resources;
import com.WorkSphere.WorkSphereBackend.entity.Users;
import com.WorkSphere.WorkSphereBackend.enums.PriorityLevel;
import com.WorkSphere.WorkSphereBackend.mapper.ResourceRequestMapper;
import com.WorkSphere.WorkSphereBackend.repository.NotificationRepository;
import com.WorkSphere.WorkSphereBackend.repository.ResourceAllocationHistoryRepository;
import com.WorkSphere.WorkSphereBackend.repository.ResourceRequestRepository;
import com.WorkSphere.WorkSphereBackend.repository.ResourcesRepository;
import com.WorkSphere.WorkSphereBackend.repository.UsersRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResourceRequestService {

    private final ResourceRequestRepository requestRepository;
    private final UsersRepository usersRepository;
    private final ResourcesRepository resourcesRepository;
    private final NotificationRepository notificationRepository;
    private final ResourceAllocationHistoryRepository resourceAllocationHistoryRepository;
    private final ResourceRequestMapper resourceRequestMapper;

    public ResourceRequestResponseDTO createResourceRequest(String userEmail,
                                                 String resourceName,
                                                 String description,
                                                 PriorityLevel priority) {

        // 1️⃣ Find User by Email
        Users user = usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ Find Resource by Name
        Resources resource = resourcesRepository.findByResourceName(resourceName);

        if (resource == null) {
            throw new RuntimeException("Resource not found with name: " + resourceName);
        }


        // 3️⃣ Create Request
        ResourceRequest request = ResourceRequest.builder()
                .user(user)
                .resource(resource)
                .requestDescription(description)
                .requestDate(LocalDateTime.now())
                .priorityLevel(priority) // if String
                .status("APPLIED")
                .build();

        ResourceRequest savedRequest = requestRepository.save(request);

        // 4️⃣ Create Notification for ADMIN
        Notification notification = Notification.builder()
                .user(user)
                .resource(resource)
                .message("New request applied for " + resourceName 
                         + " by user " + user.getName())
                .isread(false)
                .role("ADMIN")
                .build();


        notificationRepository.save(notification);

        return resourceRequestMapper.toResourceRequestResponseDTO(savedRequest);
    }
    
    
    
    @Transactional
    public void acceptRequest(Integer requestId) {

        // 1️⃣ Get request
        ResourceRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        Resources resource = request.getResource();
        ResourceCategory category = resource.getCategory();

        // 2️⃣ Check availability
        if (category.getAvailableQuantity() <= 0) {
            throw new RuntimeException("Resource not available");
        }

        // 3️⃣ Change request status → ACCEPTED
        request.setStatus("ACCEPTED");

        // 4️⃣ Send notification to user
        Notification notification = Notification.builder()
                .user(request.getUser())
                .resource(resource)
                .message("Your request for " + resource.getResourceName() + " has been accepted")
                .isread(false)
                .role("USER")
                .build();

        notificationRepository.save(notification);

        // 5️⃣ Reduce category quantity
        category.setAvailableQuantity(category.getAvailableQuantity() - 1);

        // 6️⃣ If quantity becomes 0 → update other APPLIED requests
        if (category.getAvailableQuantity() == 0) {

            requestRepository.updateOtherAppliedRequestsToAwaited(
                    resource.getResourceId(),
                    requestId
            );
        }
    }
    
    
    @Transactional
    public void allotResource(Integer requestId) {

        // 1️⃣ Fetch request
        ResourceRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        // Optional safety validation
        if (!"ACCEPTED".equals(request.getStatus())) {
            throw new RuntimeException("Only ACCEPTED requests can be allotted");
        }

        Resources resource = request.getResource();

        // 2️⃣ Create allocation history entry
        ResourceAllocationHistory allocation = ResourceAllocationHistory.builder()
                .user(request.getUser())
                .resource(resource)
                .request(request)
                .allocatedAt(LocalDateTime.now())
                .build();
        resourceAllocationHistoryRepository.save(allocation);

        // 3️⃣ Update request status → ALLOTED
        request.setStatus("ALLOTED");
        requestRepository.save(request);   // explicitly saving

        // 4️⃣ Send notification to user
        Notification notification = Notification.builder()
                .user(request.getUser())
                .resource(resource)
                .message("Your request for " + resource.getResourceName() + " has been allotted.")
                .isread(false)
                .role("USER")
                .build();

        notificationRepository.save(notification);
    }

    @Transactional
    public void returnResource(Integer requestId) {

        // 1️⃣ Fetch request
        ResourceRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!"ALLOTED".equals(request.getStatus())) {
            throw new RuntimeException("Only ALLOTED resources can be returned");
        }

        Resources resource = request.getResource();
        ResourceCategory category = resource.getCategory();

        // 2️⃣ Change request status → RETURNED
        request.setStatus("RETURNED");
        requestRepository.save(request);

        // 3️⃣ Increase available quantity
        category.setAvailableQuantity(category.getAvailableQuantity() + 1);

        // 4️⃣ Update allocation history (optional but recommended)
        resourceAllocationHistoryRepository.updateReturnTime(requestId, LocalDateTime.now());

        // 5️⃣ Move all AWAITED requests → APPLIED
        requestRepository.updateAwaitedToApplied(resource.getResourceId());
    }

}
