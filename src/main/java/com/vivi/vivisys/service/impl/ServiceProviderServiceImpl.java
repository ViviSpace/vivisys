package com.vivi.vivisys.service.impl;

import com.vivi.vivisys.service.ServiceProviderService;
import com.vivi.vivisys.domain.ServiceProvider;
import com.vivi.vivisys.repository.ServiceProviderRepository;
import com.vivi.vivisys.repository.search.ServiceProviderSearchRepository;
import com.vivi.vivisys.service.dto.ServiceProviderDTO;
import com.vivi.vivisys.service.mapper.ServiceProviderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ServiceProvider.
 */
@Service
@Transactional
public class ServiceProviderServiceImpl implements ServiceProviderService{

    private final Logger log = LoggerFactory.getLogger(ServiceProviderServiceImpl.class);

    private final ServiceProviderRepository serviceProviderRepository;

    private final ServiceProviderMapper serviceProviderMapper;

    private final ServiceProviderSearchRepository serviceProviderSearchRepository;

    public ServiceProviderServiceImpl(ServiceProviderRepository serviceProviderRepository, ServiceProviderMapper serviceProviderMapper, ServiceProviderSearchRepository serviceProviderSearchRepository) {
        this.serviceProviderRepository = serviceProviderRepository;
        this.serviceProviderMapper = serviceProviderMapper;
        this.serviceProviderSearchRepository = serviceProviderSearchRepository;
    }

    /**
     * Save a serviceProvider.
     *
     * @param serviceProviderDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ServiceProviderDTO save(ServiceProviderDTO serviceProviderDTO) {
        log.debug("Request to save ServiceProvider : {}", serviceProviderDTO);
        ServiceProvider serviceProvider = serviceProviderMapper.toEntity(serviceProviderDTO);
        serviceProvider = serviceProviderRepository.save(serviceProvider);
        ServiceProviderDTO result = serviceProviderMapper.toDto(serviceProvider);
        serviceProviderSearchRepository.save(serviceProvider);
        return result;
    }

    /**
     *  Get all the serviceProviders.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceProviderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceProviders");
        return serviceProviderRepository.findAll(pageable)
            .map(serviceProviderMapper::toDto);
    }

    /**
     *  Get one serviceProvider by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ServiceProviderDTO findOne(Long id) {
        log.debug("Request to get ServiceProvider : {}", id);
        ServiceProvider serviceProvider = serviceProviderRepository.findOne(id);
        return serviceProviderMapper.toDto(serviceProvider);
    }

    /**
     *  Delete the  serviceProvider by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ServiceProvider : {}", id);
        serviceProviderRepository.delete(id);
        serviceProviderSearchRepository.delete(id);
    }

    /**
     * Search for the serviceProvider corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceProviderDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ServiceProviders for query {}", query);
        Page<ServiceProvider> result = serviceProviderSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(serviceProviderMapper::toDto);
    }
}
