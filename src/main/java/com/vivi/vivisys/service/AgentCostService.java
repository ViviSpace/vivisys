package com.vivi.vivisys.service;

import com.vivi.vivisys.service.dto.AgentCostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing AgentCost.
 */
public interface AgentCostService {

    /**
     * Save a agentCost.
     *
     * @param agentCostDTO the entity to save
     * @return the persisted entity
     */
    AgentCostDTO save(AgentCostDTO agentCostDTO);

    /**
     *  Get all the agentCosts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AgentCostDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" agentCost.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AgentCostDTO findOne(Long id);

    /**
     *  Delete the "id" agentCost.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the agentCost corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AgentCostDTO> search(String query, Pageable pageable);
}
