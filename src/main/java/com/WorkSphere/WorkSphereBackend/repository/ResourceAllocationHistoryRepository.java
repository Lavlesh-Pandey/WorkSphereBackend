package com.WorkSphere.WorkSphereBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.WorkSphere.WorkSphereBackend.entity.ResourceAllocationHistory;
import com.WorkSphere.WorkSphereBackend.entity.Users;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface ResourceAllocationHistoryRepository extends JpaRepository<ResourceAllocationHistory, Integer> {

    List<ResourceAllocationHistory> findByUser(Users user);

    @Modifying
    @Transactional
    @Query("""
           UPDATE ResourceAllocationHistory rah
           SET rah.returnedAt = :returnTime
           WHERE rah.id = :id
           """)
    void updateReturnTime(@Param("id") Integer id,
                          @Param("returnTime") LocalDateTime returnTime);

}
