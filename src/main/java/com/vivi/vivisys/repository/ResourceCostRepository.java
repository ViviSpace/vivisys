package com.vivi.vivisys.repository;

import com.vivi.vivisys.domain.ResourceCost;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ResourceCost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceCostRepository extends JpaRepository<ResourceCost,Long> {
    
}
