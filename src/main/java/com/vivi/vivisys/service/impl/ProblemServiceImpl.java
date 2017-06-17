package com.vivi.vivisys.service.impl;

import com.vivi.vivisys.service.ProblemService;
import com.vivi.vivisys.domain.Problem;
import com.vivi.vivisys.repository.ProblemRepository;
import com.vivi.vivisys.repository.search.ProblemSearchRepository;
import com.vivi.vivisys.service.dto.ProblemDTO;
import com.vivi.vivisys.service.mapper.ProblemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Problem.
 */
@Service
@Transactional
public class ProblemServiceImpl implements ProblemService{

    private final Logger log = LoggerFactory.getLogger(ProblemServiceImpl.class);

    private final ProblemRepository problemRepository;

    private final ProblemMapper problemMapper;

    private final ProblemSearchRepository problemSearchRepository;

    public ProblemServiceImpl(ProblemRepository problemRepository, ProblemMapper problemMapper, ProblemSearchRepository problemSearchRepository) {
        this.problemRepository = problemRepository;
        this.problemMapper = problemMapper;
        this.problemSearchRepository = problemSearchRepository;
    }

    /**
     * Save a problem.
     *
     * @param problemDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProblemDTO save(ProblemDTO problemDTO) {
        log.debug("Request to save Problem : {}", problemDTO);
        Problem problem = problemMapper.toEntity(problemDTO);
        problem = problemRepository.save(problem);
        ProblemDTO result = problemMapper.toDto(problem);
        problemSearchRepository.save(problem);
        return result;
    }

    /**
     *  Get all the problems.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProblemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Problems");
        return problemRepository.findAll(pageable)
            .map(problemMapper::toDto);
    }

    /**
     *  Get one problem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProblemDTO findOne(Long id) {
        log.debug("Request to get Problem : {}", id);
        Problem problem = problemRepository.findOne(id);
        return problemMapper.toDto(problem);
    }

    /**
     *  Delete the  problem by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Problem : {}", id);
        problemRepository.delete(id);
        problemSearchRepository.delete(id);
    }

    /**
     * Search for the problem corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProblemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Problems for query {}", query);
        Page<Problem> result = problemSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(problemMapper::toDto);
    }
}
