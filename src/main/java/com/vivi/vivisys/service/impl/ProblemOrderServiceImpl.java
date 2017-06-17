package com.vivi.vivisys.service.impl;

import com.vivi.vivisys.service.ProblemOrderService;
import com.vivi.vivisys.domain.ProblemOrder;
import com.vivi.vivisys.repository.ProblemOrderRepository;
import com.vivi.vivisys.repository.search.ProblemOrderSearchRepository;
import com.vivi.vivisys.service.dto.ProblemOrderDTO;
import com.vivi.vivisys.service.mapper.ProblemOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ProblemOrder.
 */
@Service
@Transactional
public class ProblemOrderServiceImpl implements ProblemOrderService{

    private final Logger log = LoggerFactory.getLogger(ProblemOrderServiceImpl.class);

    private final ProblemOrderRepository problemOrderRepository;

    private final ProblemOrderMapper problemOrderMapper;

    private final ProblemOrderSearchRepository problemOrderSearchRepository;

    public ProblemOrderServiceImpl(ProblemOrderRepository problemOrderRepository, ProblemOrderMapper problemOrderMapper, ProblemOrderSearchRepository problemOrderSearchRepository) {
        this.problemOrderRepository = problemOrderRepository;
        this.problemOrderMapper = problemOrderMapper;
        this.problemOrderSearchRepository = problemOrderSearchRepository;
    }

    /**
     * Save a problemOrder.
     *
     * @param problemOrderDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProblemOrderDTO save(ProblemOrderDTO problemOrderDTO) {
        log.debug("Request to save ProblemOrder : {}", problemOrderDTO);
        ProblemOrder problemOrder = problemOrderMapper.toEntity(problemOrderDTO);
        problemOrder = problemOrderRepository.save(problemOrder);
        ProblemOrderDTO result = problemOrderMapper.toDto(problemOrder);
        problemOrderSearchRepository.save(problemOrder);
        return result;
    }

    /**
     *  Get all the problemOrders.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProblemOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProblemOrders");
        return problemOrderRepository.findAll(pageable)
            .map(problemOrderMapper::toDto);
    }

    /**
     *  Get one problemOrder by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProblemOrderDTO findOne(Long id) {
        log.debug("Request to get ProblemOrder : {}", id);
        ProblemOrder problemOrder = problemOrderRepository.findOne(id);
        return problemOrderMapper.toDto(problemOrder);
    }

    /**
     *  Delete the  problemOrder by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProblemOrder : {}", id);
        problemOrderRepository.delete(id);
        problemOrderSearchRepository.delete(id);
    }

    /**
     * Search for the problemOrder corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProblemOrderDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProblemOrders for query {}", query);
        Page<ProblemOrder> result = problemOrderSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(problemOrderMapper::toDto);
    }
}
