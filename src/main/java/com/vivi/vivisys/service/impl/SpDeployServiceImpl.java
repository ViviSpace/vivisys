package com.vivi.vivisys.service.impl;

import com.vivi.vivisys.service.SpDeployService;
import com.vivi.vivisys.domain.SpDeploy;
import com.vivi.vivisys.repository.SpDeployRepository;
import com.vivi.vivisys.repository.search.SpDeploySearchRepository;
import com.vivi.vivisys.service.dto.SpDeployDTO;
import com.vivi.vivisys.service.mapper.SpDeployMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SpDeploy.
 */
@Service
@Transactional
public class SpDeployServiceImpl implements SpDeployService{

    private final Logger log = LoggerFactory.getLogger(SpDeployServiceImpl.class);

    private final SpDeployRepository spDeployRepository;

    private final SpDeployMapper spDeployMapper;

    private final SpDeploySearchRepository spDeploySearchRepository;

    public SpDeployServiceImpl(SpDeployRepository spDeployRepository, SpDeployMapper spDeployMapper, SpDeploySearchRepository spDeploySearchRepository) {
        this.spDeployRepository = spDeployRepository;
        this.spDeployMapper = spDeployMapper;
        this.spDeploySearchRepository = spDeploySearchRepository;
    }

    /**
     * Save a spDeploy.
     *
     * @param spDeployDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SpDeployDTO save(SpDeployDTO spDeployDTO) {
        log.debug("Request to save SpDeploy : {}", spDeployDTO);
        SpDeploy spDeploy = spDeployMapper.toEntity(spDeployDTO);
        spDeploy = spDeployRepository.save(spDeploy);
        SpDeployDTO result = spDeployMapper.toDto(spDeploy);
        spDeploySearchRepository.save(spDeploy);
        return result;
    }

    /**
     *  Get all the spDeploys.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SpDeployDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SpDeploys");
        return spDeployRepository.findAll(pageable)
            .map(spDeployMapper::toDto);
    }

    /**
     *  Get one spDeploy by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SpDeployDTO findOne(Long id) {
        log.debug("Request to get SpDeploy : {}", id);
        SpDeploy spDeploy = spDeployRepository.findOne(id);
        return spDeployMapper.toDto(spDeploy);
    }

    /**
     *  Delete the  spDeploy by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SpDeploy : {}", id);
        spDeployRepository.delete(id);
        spDeploySearchRepository.delete(id);
    }

    /**
     * Search for the spDeploy corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SpDeployDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SpDeploys for query {}", query);
        Page<SpDeploy> result = spDeploySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(spDeployMapper::toDto);
    }
}
