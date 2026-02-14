package com.WorkSphere.WorkSphereBackend.entity;

import com.WorkSphere.WorkSphereBackend.enums.Role;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "NOTIFICATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "message")
    private String message;

    @Column(name = "isread")
    private Boolean isread;

    @Column(name = "role")
    private Role role;
}
