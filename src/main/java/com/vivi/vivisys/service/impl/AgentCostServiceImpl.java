package com.vivi.vivisys.service.impl;

import com.vivi.vivisys.service.AgentCostService;
import com.vivi.vivisys.domain.AgentCost;
import com.vivi.vivisys.repository.AgentCostRepository;
import com.vivi.vivisys.repository.search.AgentCostSearchRepository;
import com.vivi.vivisys.service.dto.AgentCostDTO;
import com.vivi.vivisys.service.mapper.AgentCostMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AgentCost.
 */
@Service
@Transactional
public class AgentCostServiceImpl implements AgentCostService{

    private final Logger log = LoggerFactory.getLogger(AgentCostServiceImpl.class);

    private final AgentCostRepository agentCostRepository;

    private final AgentCostMapper agentCostMapper;

    private final AgentCostSearchRepository agentCostSearchRepository;

    public AgentCostServiceImpl(AgentCostRepository agentCostRepository, AgentCostMapper agentCostMapper, AgentCostSearchRepository agentCostSearchRepository) {
        this.agentCostRepository = agentCostRepository;
        this.agentCostMapper = agentCostMapper;
        this.agentCostSearchRepository = agentCostSearchRepository;
    }

    /**
     * Save a agentCost.
     *
     * @param agentCostDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AgentCostDTO save(AgentCostDTO agentCostDTO) {
        log.debug("Request to save AgentCost : {}", agentCostDTO);
        AgentCost agentCost = agentCostMapper.toEntity(agentCostDTO);
        agentCost = agentCostRepository.save(agentCost);
        AgentCostDTO result = agentCostMapper.toDto(agentCost);
        agentCostSearchRepository.save(agentCost);
        return result;
    }

    /**
     *  Get all the agentCosts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AgentCostDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AgentCosts");
        return agentCostRepository.findAll(pageable)
            .map(agentCostMapper::toDto);
    }

    /**
     *  Get one agentCost by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AgentCostDTO findOne(Long id) {
        log.debug("Request to get AgentCost : {}", id);
        AgentCost agentCost = agentCostRepository.findOne(id);
        return agentCostMapper.toDto(agentCost);
    }

    /**
     *  Delete the  agentCost by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AgentCost : {}", id);
        agentCostRepository.delete(id);
        agentCostSearchRepository.delete(id);
    }

    /**
     * Search for the agentCost corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AgentCostDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AgentCosts for query {}", query);
        Page<AgentCost> result = agentCostSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(agentCostMapper::toDto);
    }
}
