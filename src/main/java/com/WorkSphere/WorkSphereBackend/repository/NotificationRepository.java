package com.WorkSphere.WorkSphereBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.WorkSphere.WorkSphereBackend.entity.Notification;
import com.WorkSphere.WorkSphereBackend.entity.Users;
import com.WorkSphere.WorkSphereBackend.enums.Role;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {


    List<Notification> findByUserAndIsread(Users user, Boolean isread);

    List<Notification> findByRoleAndIsread(Role role, Boolean isread);

}
