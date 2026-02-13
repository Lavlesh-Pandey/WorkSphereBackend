package com.WorkSphere.WorkSphereBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.WorkSphere.WorkSphereBackend.entity.Resources;

public interface ResourcesRepository extends JpaRepository<Resources, Integer> {

    Resources findByResourceName(String resourceName);

}
