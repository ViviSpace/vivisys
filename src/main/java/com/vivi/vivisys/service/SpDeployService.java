package com.vivi.vivisys.service;

import com.vivi.vivisys.service.dto.SpDeployDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing SpDeploy.
 */
public interface SpDeployService {

    /**
     * Save a spDeploy.
     *
     * @param spDeployDTO the entity to save
     * @return the persisted entity
     */
    SpDeployDTO save(SpDeployDTO spDeployDTO);

    /**
     *  Get all the spDeploys.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SpDeployDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" spDeploy.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SpDeployDTO findOne(Long id);

    /**
     *  Delete the "id" spDeploy.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the spDeploy corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SpDeployDTO> search(String query, Pageable pageable);
}
