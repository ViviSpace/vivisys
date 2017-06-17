package com.vivi.vivisys.service;

import com.vivi.vivisys.service.dto.SpCostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing SpCost.
 */
public interface SpCostService {

    /**
     * Save a spCost.
     *
     * @param spCostDTO the entity to save
     * @return the persisted entity
     */
    SpCostDTO save(SpCostDTO spCostDTO);

    /**
     *  Get all the spCosts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SpCostDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" spCost.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SpCostDTO findOne(Long id);

    /**
     *  Delete the "id" spCost.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the spCost corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SpCostDTO> search(String query, Pageable pageable);
}
