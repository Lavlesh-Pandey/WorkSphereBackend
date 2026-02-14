package com.WorkSphere.WorkSphereBackend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.WorkSphere.WorkSphereBackend.entity.Notification;
import com.WorkSphere.WorkSphereBackend.entity.Users;
import com.WorkSphere.WorkSphereBackend.enums.Role;
import com.WorkSphere.WorkSphereBackend.repository.NotificationRepository;
import com.WorkSphere.WorkSphereBackend.repository.UsersRepository;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public List<Notification> getUnreadNotifications(Integer userId) {

        // 1️⃣ Fetch user
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Notification> notifications;

        // 2️⃣ Check role from DB
        if (Role.ADMIN.equals(user.getRole())) {

            notifications = notificationRepository
                    .findByRoleAndIsread(Role.ADMIN, false);

        } else {

            notifications = notificationRepository
                    .findByUserAndIsread(user, false);
        }

        // 3️⃣ Mark as read
        for (Notification n : notifications) {
            n.setIsread(true);
        }

        // No save needed because of @Transactional

        return notifications;
    }
}

