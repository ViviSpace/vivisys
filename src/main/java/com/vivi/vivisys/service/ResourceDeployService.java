package com.vivi.vivisys.service;

import com.vivi.vivisys.service.dto.ResourceDeployDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ResourceDeploy.
 */
public interface ResourceDeployService {

    /**
     * Save a resourceDeploy.
     *
     * @param resourceDeployDTO the entity to save
     * @return the persisted entity
     */
    ResourceDeployDTO save(ResourceDeployDTO resourceDeployDTO);

    /**
     *  Get all the resourceDeploys.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ResourceDeployDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" resourceDeploy.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ResourceDeployDTO findOne(Long id);

    /**
     *  Delete the "id" resourceDeploy.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the resourceDeploy corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ResourceDeployDTO> search(String query, Pageable pageable);
}
