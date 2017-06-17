package com.vivi.vivisys.service.impl;

import com.vivi.vivisys.service.SpCostService;
import com.vivi.vivisys.domain.SpCost;
import com.vivi.vivisys.repository.SpCostRepository;
import com.vivi.vivisys.repository.search.SpCostSearchRepository;
import com.vivi.vivisys.service.dto.SpCostDTO;
import com.vivi.vivisys.service.mapper.SpCostMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SpCost.
 */
@Service
@Transactional
public class SpCostServiceImpl implements SpCostService{

    private final Logger log = LoggerFactory.getLogger(SpCostServiceImpl.class);

    private final SpCostRepository spCostRepository;

    private final SpCostMapper spCostMapper;

    private final SpCostSearchRepository spCostSearchRepository;

    public SpCostServiceImpl(SpCostRepository spCostRepository, SpCostMapper spCostMapper, SpCostSearchRepository spCostSearchRepository) {
        this.spCostRepository = spCostRepository;
        this.spCostMapper = spCostMapper;
        this.spCostSearchRepository = spCostSearchRepository;
    }

    /**
     * Save a spCost.
     *
     * @param spCostDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SpCostDTO save(SpCostDTO spCostDTO) {
        log.debug("Request to save SpCost : {}", spCostDTO);
        SpCost spCost = spCostMapper.toEntity(spCostDTO);
        spCost = spCostRepository.save(spCost);
        SpCostDTO result = spCostMapper.toDto(spCost);
        spCostSearchRepository.save(spCost);
        return result;
    }

    /**
     *  Get all the spCosts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SpCostDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SpCosts");
        return spCostRepository.findAll(pageable)
            .map(spCostMapper::toDto);
    }

    /**
     *  Get one spCost by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SpCostDTO findOne(Long id) {
        log.debug("Request to get SpCost : {}", id);
        SpCost spCost = spCostRepository.findOne(id);
        return spCostMapper.toDto(spCost);
    }

    /**
     *  Delete the  spCost by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SpCost : {}", id);
        spCostRepository.delete(id);
        spCostSearchRepository.delete(id);
    }

    /**
     * Search for the spCost corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SpCostDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SpCosts for query {}", query);
        Page<SpCost> result = spCostSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(spCostMapper::toDto);
    }
}
