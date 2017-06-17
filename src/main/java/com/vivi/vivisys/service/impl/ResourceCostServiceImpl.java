package com.vivi.vivisys.service.impl;

import com.vivi.vivisys.service.ResourceCostService;
import com.vivi.vivisys.domain.ResourceCost;
import com.vivi.vivisys.repository.ResourceCostRepository;
import com.vivi.vivisys.repository.search.ResourceCostSearchRepository;
import com.vivi.vivisys.service.dto.ResourceCostDTO;
import com.vivi.vivisys.service.mapper.ResourceCostMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ResourceCost.
 */
@Service
@Transactional
public class ResourceCostServiceImpl implements ResourceCostService{

    private final Logger log = LoggerFactory.getLogger(ResourceCostServiceImpl.class);

    private final ResourceCostRepository resourceCostRepository;

    private final ResourceCostMapper resourceCostMapper;

    private final ResourceCostSearchRepository resourceCostSearchRepository;

    public ResourceCostServiceImpl(ResourceCostRepository resourceCostRepository, ResourceCostMapper resourceCostMapper, ResourceCostSearchRepository resourceCostSearchRepository) {
        this.resourceCostRepository = resourceCostRepository;
        this.resourceCostMapper = resourceCostMapper;
        this.resourceCostSearchRepository = resourceCostSearchRepository;
    }

    /**
     * Save a resourceCost.
     *
     * @param resourceCostDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ResourceCostDTO save(ResourceCostDTO resourceCostDTO) {
        log.debug("Request to save ResourceCost : {}", resourceCostDTO);
        ResourceCost resourceCost = resourceCostMapper.toEntity(resourceCostDTO);
        resourceCost = resourceCostRepository.save(resourceCost);
        ResourceCostDTO result = resourceCostMapper.toDto(resourceCost);
        resourceCostSearchRepository.save(resourceCost);
        return result;
    }

    /**
     *  Get all the resourceCosts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ResourceCostDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ResourceCosts");
        return resourceCostRepository.findAll(pageable)
            .map(resourceCostMapper::toDto);
    }

    /**
     *  Get one resourceCost by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ResourceCostDTO findOne(Long id) {
        log.debug("Request to get ResourceCost : {}", id);
        ResourceCost resourceCost = resourceCostRepository.findOne(id);
        return resourceCostMapper.toDto(resourceCost);
    }

    /**
     *  Delete the  resourceCost by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ResourceCost : {}", id);
        resourceCostRepository.delete(id);
        resourceCostSearchRepository.delete(id);
    }

    /**
     * Search for the resourceCost corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ResourceCostDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ResourceCosts for query {}", query);
        Page<ResourceCost> result = resourceCostSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(resourceCostMapper::toDto);
    }
}
