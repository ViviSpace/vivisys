package com.vivi.vivisys.service.impl;

import com.vivi.vivisys.service.ResourceDeployService;
import com.vivi.vivisys.domain.ResourceDeploy;
import com.vivi.vivisys.repository.ResourceDeployRepository;
import com.vivi.vivisys.repository.search.ResourceDeploySearchRepository;
import com.vivi.vivisys.service.dto.ResourceDeployDTO;
import com.vivi.vivisys.service.mapper.ResourceDeployMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ResourceDeploy.
 */
@Service
@Transactional
public class ResourceDeployServiceImpl implements ResourceDeployService{

    private final Logger log = LoggerFactory.getLogger(ResourceDeployServiceImpl.class);

    private final ResourceDeployRepository resourceDeployRepository;

    private final ResourceDeployMapper resourceDeployMapper;

    private final ResourceDeploySearchRepository resourceDeploySearchRepository;

    public ResourceDeployServiceImpl(ResourceDeployRepository resourceDeployRepository, ResourceDeployMapper resourceDeployMapper, ResourceDeploySearchRepository resourceDeploySearchRepository) {
        this.resourceDeployRepository = resourceDeployRepository;
        this.resourceDeployMapper = resourceDeployMapper;
        this.resourceDeploySearchRepository = resourceDeploySearchRepository;
    }

    /**
     * Save a resourceDeploy.
     *
     * @param resourceDeployDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ResourceDeployDTO save(ResourceDeployDTO resourceDeployDTO) {
        log.debug("Request to save ResourceDeploy : {}", resourceDeployDTO);
        ResourceDeploy resourceDeploy = resourceDeployMapper.toEntity(resourceDeployDTO);
        resourceDeploy = resourceDeployRepository.save(resourceDeploy);
        ResourceDeployDTO result = resourceDeployMapper.toDto(resourceDeploy);
        resourceDeploySearchRepository.save(resourceDeploy);
        return result;
    }

    /**
     *  Get all the resourceDeploys.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ResourceDeployDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ResourceDeploys");
        return resourceDeployRepository.findAll(pageable)
            .map(resourceDeployMapper::toDto);
    }

    /**
     *  Get one resourceDeploy by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ResourceDeployDTO findOne(Long id) {
        log.debug("Request to get ResourceDeploy : {}", id);
        ResourceDeploy resourceDeploy = resourceDeployRepository.findOne(id);
        return resourceDeployMapper.toDto(resourceDeploy);
    }

    /**
     *  Delete the  resourceDeploy by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ResourceDeploy : {}", id);
        resourceDeployRepository.delete(id);
        resourceDeploySearchRepository.delete(id);
    }

    /**
     * Search for the resourceDeploy corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ResourceDeployDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ResourceDeploys for query {}", query);
        Page<ResourceDeploy> result = resourceDeploySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(resourceDeployMapper::toDto);
    }
}
