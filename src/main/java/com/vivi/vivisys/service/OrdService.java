package com.vivi.vivisys.service;

import com.vivi.vivisys.service.dto.OrdDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Ord.
 */
public interface OrdService {

    /**
     * Save a ord.
     *
     * @param ordDTO the entity to save
     * @return the persisted entity
     */
    OrdDTO save(OrdDTO ordDTO);

    /**
     *  Get all the ords.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<OrdDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" ord.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    OrdDTO findOne(Long id);

    /**
     *  Delete the "id" ord.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the ord corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<OrdDTO> search(String query, Pageable pageable);
}
