package com.vivi.vivisys.service;

import com.vivi.vivisys.service.dto.ProblemOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ProblemOrder.
 */
public interface ProblemOrderService {

    /**
     * Save a problemOrder.
     *
     * @param problemOrderDTO the entity to save
     * @return the persisted entity
     */
    ProblemOrderDTO save(ProblemOrderDTO problemOrderDTO);

    /**
     *  Get all the problemOrders.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ProblemOrderDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" problemOrder.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProblemOrderDTO findOne(Long id);

    /**
     *  Delete the "id" problemOrder.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the problemOrder corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ProblemOrderDTO> search(String query, Pageable pageable);
}
