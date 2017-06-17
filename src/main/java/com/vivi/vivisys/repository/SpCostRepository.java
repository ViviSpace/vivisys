package com.vivi.vivisys.repository;

import com.vivi.vivisys.domain.SpCost;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SpCost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpCostRepository extends JpaRepository<SpCost,Long> {
    
}
