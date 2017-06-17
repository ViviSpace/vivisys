package com.vivi.vivisys.service;

import com.vivi.vivisys.service.dto.ServDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Serv.
 */
public interface ServService {

    /**
     * Save a serv.
     *
     * @param servDTO the entity to save
     * @return the persisted entity
     */
    ServDTO save(ServDTO servDTO);

    /**
     *  Get all the servs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ServDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" serv.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ServDTO findOne(Long id);

    /**
     *  Delete the "id" serv.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the serv corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ServDTO> search(String query, Pageable pageable);
}
