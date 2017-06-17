package com.vivi.vivisys.service.impl;

import com.vivi.vivisys.service.ServService;
import com.vivi.vivisys.domain.Serv;
import com.vivi.vivisys.repository.ServRepository;
import com.vivi.vivisys.repository.search.ServSearchRepository;
import com.vivi.vivisys.service.dto.ServDTO;
import com.vivi.vivisys.service.mapper.ServMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Serv.
 */
@Service
@Transactional
public class ServServiceImpl implements ServService{

    private final Logger log = LoggerFactory.getLogger(ServServiceImpl.class);

    private final ServRepository servRepository;

    private final ServMapper servMapper;

    private final ServSearchRepository servSearchRepository;

    public ServServiceImpl(ServRepository servRepository, ServMapper servMapper, ServSearchRepository servSearchRepository) {
        this.servRepository = servRepository;
        this.servMapper = servMapper;
        this.servSearchRepository = servSearchRepository;
    }

    /**
     * Save a serv.
     *
     * @param servDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ServDTO save(ServDTO servDTO) {
        log.debug("Request to save Serv : {}", servDTO);
        Serv serv = servMapper.toEntity(servDTO);
        serv = servRepository.save(serv);
        ServDTO result = servMapper.toDto(serv);
        servSearchRepository.save(serv);
        return result;
    }

    /**
     *  Get all the servs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ServDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Servs");
        return servRepository.findAll(pageable)
            .map(servMapper::toDto);
    }

    /**
     *  Get one serv by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ServDTO findOne(Long id) {
        log.debug("Request to get Serv : {}", id);
        Serv serv = servRepository.findOneWithEagerRelationships(id);
        return servMapper.toDto(serv);
    }

    /**
     *  Delete the  serv by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Serv : {}", id);
        servRepository.delete(id);
        servSearchRepository.delete(id);
    }

    /**
     * Search for the serv corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ServDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Servs for query {}", query);
        Page<Serv> result = servSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(servMapper::toDto);
    }
}
