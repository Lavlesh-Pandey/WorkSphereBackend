package com.WorkSphere.WorkSphereBackend.entity;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    private Resources resource;

    @Column(name = "message")
    private String message;

    @Column(name = "isread")
    private Boolean isread;

    @Column(name = "role")
    private String role;
}
