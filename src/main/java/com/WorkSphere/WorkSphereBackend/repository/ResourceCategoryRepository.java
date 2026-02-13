package com.WorkSphere.WorkSphereBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.WorkSphere.WorkSphereBackend.entity.ResourceCategory;
import java.util.Optional;

public interface ResourceCategoryRepository extends JpaRepository<ResourceCategory, Integer> {

    Optional<ResourceCategory> findByCategoryName(String categoryName);

}
