package com.vivi.vivisys.service.impl;

import com.vivi.vivisys.service.AgentService;
import com.vivi.vivisys.domain.Agent;
import com.vivi.vivisys.repository.AgentRepository;
import com.vivi.vivisys.repository.search.AgentSearchRepository;
import com.vivi.vivisys.service.dto.AgentDTO;
import com.vivi.vivisys.service.mapper.AgentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Agent.
 */
@Service
@Transactional
public class AgentServiceImpl implements AgentService{

    private final Logger log = LoggerFactory.getLogger(AgentServiceImpl.class);

    private final AgentRepository agentRepository;

    private final AgentMapper agentMapper;

    private final AgentSearchRepository agentSearchRepository;

    public AgentServiceImpl(AgentRepository agentRepository, AgentMapper agentMapper, AgentSearchRepository agentSearchRepository) {
        this.agentRepository = agentRepository;
        this.agentMapper = agentMapper;
        this.agentSearchRepository = agentSearchRepository;
    }

    /**
     * Save a agent.
     *
     * @param agentDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AgentDTO save(AgentDTO agentDTO) {
        log.debug("Request to save Agent : {}", agentDTO);
        Agent agent = agentMapper.toEntity(agentDTO);
        agent = agentRepository.save(agent);
        AgentDTO result = agentMapper.toDto(agent);
        agentSearchRepository.save(agent);
        return result;
    }

    /**
     *  Get all the agents.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AgentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Agents");
        return agentRepository.findAll(pageable)
            .map(agentMapper::toDto);
    }

    /**
     *  Get one agent by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AgentDTO findOne(Long id) {
        log.debug("Request to get Agent : {}", id);
        Agent agent = agentRepository.findOne(id);
        return agentMapper.toDto(agent);
    }

    /**
     *  Delete the  agent by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Agent : {}", id);
        agentRepository.delete(id);
        agentSearchRepository.delete(id);
    }

    /**
     * Search for the agent corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AgentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Agents for query {}", query);
        Page<Agent> result = agentSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(agentMapper::toDto);
    }
}
