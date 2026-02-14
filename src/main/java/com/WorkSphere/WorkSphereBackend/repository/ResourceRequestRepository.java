package com.WorkSphere.WorkSphereBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.WorkSphere.WorkSphereBackend.entity.ResourceRequest;
import com.WorkSphere.WorkSphereBackend.entity.Users;

import jakarta.transaction.Transactional;

import java.util.List;

public interface ResourceRequestRepository extends JpaRepository<ResourceRequest, Integer> {

    List<ResourceRequest> findByUser(Users user);

	@Modifying
    @Transactional
    @Query("""
           UPDATE ResourceRequest rr
           SET rr.requestStatus = com.WorkSphere.WorkSphereBackend.enums.RequestStatus.AWAITED
           WHERE rr.resource.resourceId = :resourceId
           AND rr.requestStatus = com.WorkSphere.WorkSphereBackend.enums.RequestStatus.APPLIED
           AND rr.requestId <> :requestId
           """)
    void updateOtherAppliedRequestsToAwaited(@Param("resourceId") Integer resourceId,
                                             @Param("requestId") Integer requestId);
	
	@Modifying
    @Transactional
    @Query("""
           UPDATE ResourceRequest rr
           SET rr.requestStatus = com.WorkSphere.WorkSphereBackend.enums.RequestStatus.APPLIED
           WHERE rr.resource.resourceId = :resourceId
           AND rr.requestStatus = com.WorkSphere.WorkSphereBackend.enums.RequestStatus.AWAITED
           """)
    void updateAwaitedToApplied(@Param("resourceId") Integer resourceId);
	
	@Query("""
		       SELECT r FROM ResourceRequest r
		       WHERE r.requestStatus = 'APPLIED'
		       ORDER BY 
		           CASE r.priorityLevel
		               WHEN 'HIGH' THEN 1
		               WHEN 'MEDIUM' THEN 2
		               WHEN 'LOW' THEN 3
		           END,
		           r.requestDate ASC
		       """)
		List<ResourceRequest> findAllPendingOrdered();


}
