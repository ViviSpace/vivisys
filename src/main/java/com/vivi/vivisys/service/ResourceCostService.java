package com.vivi.vivisys.service;

import com.vivi.vivisys.service.dto.ResourceCostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ResourceCost.
 */
public interface ResourceCostService {

    /**
     * Save a resourceCost.
     *
     * @param resourceCostDTO the entity to save
     * @return the persisted entity
     */
    ResourceCostDTO save(ResourceCostDTO resourceCostDTO);

    /**
     *  Get all the resourceCosts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ResourceCostDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" resourceCost.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ResourceCostDTO findOne(Long id);

    /**
     *  Delete the "id" resourceCost.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the resourceCost corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ResourceCostDTO> search(String query, Pageable pageable);
}
