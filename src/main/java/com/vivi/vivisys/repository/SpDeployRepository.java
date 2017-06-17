package com.vivi.vivisys.repository;

import com.vivi.vivisys.domain.SpDeploy;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SpDeploy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpDeployRepository extends JpaRepository<SpDeploy,Long> {
    
}
