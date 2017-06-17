package com.vivi.vivisys.repository;

import com.vivi.vivisys.domain.Ord;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Ord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdRepository extends JpaRepository<Ord,Long> {
    
}
