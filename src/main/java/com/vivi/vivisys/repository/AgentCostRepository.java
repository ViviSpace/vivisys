package com.vivi.vivisys.repository;

import com.vivi.vivisys.domain.AgentCost;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AgentCost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgentCostRepository extends JpaRepository<AgentCost,Long> {
    
}
