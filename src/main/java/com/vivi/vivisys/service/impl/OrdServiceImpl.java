package com.vivi.vivisys.service.impl;

import com.vivi.vivisys.service.OrdService;
import com.vivi.vivisys.domain.Ord;
import com.vivi.vivisys.repository.OrdRepository;
import com.vivi.vivisys.repository.search.OrdSearchRepository;
import com.vivi.vivisys.service.dto.OrdDTO;
import com.vivi.vivisys.service.mapper.OrdMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Ord.
 */
@Service
@Transactional
public class OrdServiceImpl implements OrdService{

    private final Logger log = LoggerFactory.getLogger(OrdServiceImpl.class);

    private final OrdRepository ordRepository;

    private final OrdMapper ordMapper;

    private final OrdSearchRepository ordSearchRepository;

    public OrdServiceImpl(OrdRepository ordRepository, OrdMapper ordMapper, OrdSearchRepository ordSearchRepository) {
        this.ordRepository = ordRepository;
        this.ordMapper = ordMapper;
        this.ordSearchRepository = ordSearchRepository;
    }

    /**
     * Save a ord.
     *
     * @param ordDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OrdDTO save(OrdDTO ordDTO) {
        log.debug("Request to save Ord : {}", ordDTO);
        Ord ord = ordMapper.toEntity(ordDTO);
        ord = ordRepository.save(ord);
        OrdDTO result = ordMapper.toDto(ord);
        ordSearchRepository.save(ord);
        return result;
    }

    /**
     *  Get all the ords.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrdDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ords");
        return ordRepository.findAll(pageable)
            .map(ordMapper::toDto);
    }

    /**
     *  Get one ord by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public OrdDTO findOne(Long id) {
        log.debug("Request to get Ord : {}", id);
        Ord ord = ordRepository.findOne(id);
        return ordMapper.toDto(ord);
    }

    /**
     *  Delete the  ord by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ord : {}", id);
        ordRepository.delete(id);
        ordSearchRepository.delete(id);
    }

    /**
     * Search for the ord corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrdDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Ords for query {}", query);
        Page<Ord> result = ordSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(ordMapper::toDto);
    }
}
