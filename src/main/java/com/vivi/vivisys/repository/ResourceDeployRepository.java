package com.vivi.vivisys.repository;

import com.vivi.vivisys.domain.ResourceDeploy;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ResourceDeploy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceDeployRepository extends JpaRepository<ResourceDeploy,Long> {
    
}
