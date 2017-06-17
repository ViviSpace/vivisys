package com.vivi.vivisys.repository;

import com.vivi.vivisys.domain.ProblemOrder;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProblemOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProblemOrderRepository extends JpaRepository<ProblemOrder,Long> {
    
}
