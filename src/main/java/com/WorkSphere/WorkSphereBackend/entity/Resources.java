package com.WorkSphere.WorkSphereBackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RESOURCES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resources {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Integer resourceId;

    @Column(name = "resource_name")
    private String resourceName;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ResourceCategory category;

    @Column(name = "occupied", nullable = false)
    private Boolean occupied = false;
}
