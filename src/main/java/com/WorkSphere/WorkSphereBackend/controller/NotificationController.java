package com.WorkSphere.WorkSphereBackend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.WorkSphere.WorkSphereBackend.entity.Notification;
import com.WorkSphere.WorkSphereBackend.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // ✅ GET /api/notification/{user_id}
    @GetMapping("/{user_id}")
    public List<Notification> getUnreadNotifications(
            @PathVariable("user_id") Integer userId) {

        List<Notification> notifications =
                notificationService.getUnreadNotifications(userId);
        
        return notifications;
    }
}
