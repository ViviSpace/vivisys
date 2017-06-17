package com.vivi.vivisys.service;

import com.vivi.vivisys.service.dto.ProblemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Problem.
 */
public interface ProblemService {

    /**
     * Save a problem.
     *
     * @param problemDTO the entity to save
     * @return the persisted entity
     */
    ProblemDTO save(ProblemDTO problemDTO);

    /**
     *  Get all the problems.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ProblemDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" problem.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProblemDTO findOne(Long id);

    /**
     *  Delete the "id" problem.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the problem corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ProblemDTO> search(String query, Pageable pageable);
}
