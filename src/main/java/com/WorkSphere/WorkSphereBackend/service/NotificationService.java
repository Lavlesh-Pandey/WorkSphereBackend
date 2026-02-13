package com.WorkSphere.WorkSphereBackend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.WorkSphere.WorkSphereBackend.entity.Notification;
import com.WorkSphere.WorkSphereBackend.entity.Users;
import com.WorkSphere.WorkSphereBackend.repository.NotificationRepository;
import com.WorkSphere.WorkSphereBackend.repository.UsersRepository;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public List<Notification> getUnreadNotifications(Integer userId, String role) {

        List<Notification> notifications;

        if ("ADMIN".equalsIgnoreCase(role)) {

            notifications = notificationRepository
                    .findByRoleAndIsread("ADMIN", false);

        } else {

            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            notifications = notificationRepository
                    .findByUserAndIsread(user, false);
        }

        // Mark as read
        notifications.forEach(n -> n.setIsread(true));

        return notifications; // no need saveAll because of @Transactional
    }
}
