package com.WorkSphere.WorkSphereBackend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.WorkSphere.WorkSphereBackend.dto.PendingRequestDto;
import com.WorkSphere.WorkSphereBackend.dto.ResourceRequestResponseDto;
import com.WorkSphere.WorkSphereBackend.entity.Notification;
import com.WorkSphere.WorkSphereBackend.entity.ResourceAllocationHistory;
import com.WorkSphere.WorkSphereBackend.entity.ResourceCategory;
import com.WorkSphere.WorkSphereBackend.entity.ResourceRequest;
import com.WorkSphere.WorkSphereBackend.entity.Resources;
import com.WorkSphere.WorkSphereBackend.entity.Users;
import com.WorkSphere.WorkSphereBackend.enums.PriorityLevel;
import com.WorkSphere.WorkSphereBackend.enums.RequestStatus;
import com.WorkSphere.WorkSphereBackend.enums.Role;
import com.WorkSphere.WorkSphereBackend.mapper.ResourceRequestMapper;
import com.WorkSphere.WorkSphereBackend.repository.NotificationRepository;
import com.WorkSphere.WorkSphereBackend.repository.ResourceAllocationHistoryRepository;
import com.WorkSphere.WorkSphereBackend.repository.ResourceCategoryRepository;
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
    private final ResourceCategoryRepository resourceCategoryRepository;
    private final ResourceRequestMapper resourceRequestMapper;

    public ResourceRequestResponseDto createResourceRequest(
            String userEmail,
            String categoryName,
            String description,
            PriorityLevel priority) {

        // 1️⃣ Find User by Email
        Users user = usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ Find ResourceCategory by Name
        ResourceCategory category = resourceCategoryRepository
                .findByCategoryName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // 3️⃣ Create Resource Request
        ResourceRequest request = ResourceRequest.builder()
                .user(user)
                .category(category)
                .requestDescription(description)
                .requestDate(LocalDateTime.now())
                .priorityLevel(priority)
                .requestStatus(RequestStatus.APPLIED)   // ✅ according to your enum
                .build();

        ResourceRequest savedRequest = requestRepository.save(request);

        // 4️⃣ Create Notification for ADMIN
        Notification notification = Notification.builder()
                .user(user)
                .message("New request applied for category "
                        + category.getCategoryName()
                        + " by user " + user.getName())
                .isread(false)
                .role(Role.ADMIN)   // ✅ use enum properly
                .build();

        notificationRepository.save(notification);

        return resourceRequestMapper
                .toResourceRequestResponseDTO(savedRequest);
    }

    
    
    
    @Transactional
    public void acceptRequest(Integer requestId) {

        // 1️⃣ Get request
        ResourceRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        ResourceCategory category = request.getCategory();

        // 2️⃣ Check category availability
        if (category.getAvailableQuantity() <= 0) {
            throw new RuntimeException("No resources available in this category");
        }

        // 3️⃣ Find a free resource from that category (occupied = false)
        Resources freeResource = resourcesRepository
                .findFirstByCategoryCategoryIdAndOccupiedFalse(
                        category.getCategoryId());

        if (freeResource == null) {
            throw new RuntimeException("No free resource found");
        }

        // 4️⃣ Assign that resource
        request.setResource(freeResource);

        // 5️⃣ Mark resource as occupied
        freeResource.setOccupied(true);

        // 6️⃣ Update request status → Accepted
        request.setRequestStatus(RequestStatus.ACCEPTED);

        // 7️⃣ Reduce category quantity
        category.setAvailableQuantity(
                category.getAvailableQuantity() - 1
        );

        // 8️⃣ Send notification
        Notification notification = Notification.builder()
                .user(request.getUser())
                .message("Your request has been ACCEPTED. Resource allocated: "
                        + freeResource.getResourceName())
                .isread(false)
                .role(Role.EMPLOYEE)
                .build();

        notificationRepository.save(notification);

        // 9️⃣ If quantity = 0 → update other APPLIED → AWAITED
        if (category.getAvailableQuantity() == 0) {

            requestRepository.updateOtherAppliedRequestsToAwaited(
                    category.getCategoryId(),
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
        if (!RequestStatus.ACCEPTED.equals(request.getRequestStatus())) {
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
        request.setRequestStatus(RequestStatus.ALLOCATED);
        requestRepository.save(request);   // explicitly saving

        // 4️⃣ Send notification to user
        Notification notification = Notification.builder()
                .user(request.getUser())
                .message("Your request for " + resource.getResourceName() + " has been allotted.")
                .isread(false)
                .role(Role.EMPLOYEE)
                .build();

        notificationRepository.save(notification);
    }

    @Transactional
    public void returnResource(Integer requestId) {

        // 1️⃣ Fetch request
        ResourceRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!RequestStatus.ALLOCATED.equals(request.getRequestStatus())) {
            throw new RuntimeException("Only ALLOCATED resources can be returned");
        }

        Resources resource = request.getResource();
        ResourceCategory category = resource.getCategory();

        // 2️⃣ Change request status → RETURNED
        request.setRequestStatus(RequestStatus.RETURNED);
        requestRepository.save(request);

        // 3️⃣ FREE THE RESOURCE ✅
        resource.setOccupied(false);
        resourcesRepository.save(resource);

        // 4️⃣ Increase available quantity
        category.setAvailableQuantity(category.getAvailableQuantity() + 1);
        resourceCategoryRepository.save(category);

        // 5️⃣ Send notification to employee
        Notification notification = Notification.builder()
                .user(request.getUser())
                .message("You have returned " + resource.getResourceName())
                .isread(false)
                .role(Role.EMPLOYEE)
                .build();

        notificationRepository.save(notification);

        // 6️⃣ Update allocation history
        resourceAllocationHistoryRepository
                .updateReturnTime(requestId, LocalDateTime.now());

        // 7️⃣ Move all AWAITED → APPLIED
        requestRepository.updateAwaitedToApplied(resource.getResourceId());
    }

    
    
    public List<PendingRequestDto> getAllPendingRequests() {

        List<ResourceRequest> requests =
                requestRepository.findAllPendingOrdered();

        List<PendingRequestDto> dtoList = new ArrayList<>();

        for (ResourceRequest request : requests) {
            dtoList.add(resourceRequestMapper.toPendingRequestDTO(request));
        }

        return dtoList;
    }


}
