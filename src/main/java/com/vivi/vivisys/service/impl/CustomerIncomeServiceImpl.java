package com.vivi.vivisys.service.impl;

import com.vivi.vivisys.service.CustomerIncomeService;
import com.vivi.vivisys.domain.CustomerIncome;
import com.vivi.vivisys.repository.CustomerIncomeRepository;
import com.vivi.vivisys.repository.search.CustomerIncomeSearchRepository;
import com.vivi.vivisys.service.dto.CustomerIncomeDTO;
import com.vivi.vivisys.service.mapper.CustomerIncomeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CustomerIncome.
 */
@Service
@Transactional
public class CustomerIncomeServiceImpl implements CustomerIncomeService{

    private final Logger log = LoggerFactory.getLogger(CustomerIncomeServiceImpl.class);

    private final CustomerIncomeRepository customerIncomeRepository;

    private final CustomerIncomeMapper customerIncomeMapper;

    private final CustomerIncomeSearchRepository customerIncomeSearchRepository;

    public CustomerIncomeServiceImpl(CustomerIncomeRepository customerIncomeRepository, CustomerIncomeMapper customerIncomeMapper, CustomerIncomeSearchRepository customerIncomeSearchRepository) {
        this.customerIncomeRepository = customerIncomeRepository;
        this.customerIncomeMapper = customerIncomeMapper;
        this.customerIncomeSearchRepository = customerIncomeSearchRepository;
    }

    /**
     * Save a customerIncome.
     *
     * @param customerIncomeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CustomerIncomeDTO save(CustomerIncomeDTO customerIncomeDTO) {
        log.debug("Request to save CustomerIncome : {}", customerIncomeDTO);
        CustomerIncome customerIncome = customerIncomeMapper.toEntity(customerIncomeDTO);
        customerIncome = customerIncomeRepository.save(customerIncome);
        CustomerIncomeDTO result = customerIncomeMapper.toDto(customerIncome);
        customerIncomeSearchRepository.save(customerIncome);
        return result;
    }

    /**
     *  Get all the customerIncomes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomerIncomeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerIncomes");
        return customerIncomeRepository.findAll(pageable)
            .map(customerIncomeMapper::toDto);
    }

    /**
     *  Get one customerIncome by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CustomerIncomeDTO findOne(Long id) {
        log.debug("Request to get CustomerIncome : {}", id);
        CustomerIncome customerIncome = customerIncomeRepository.findOne(id);
        return customerIncomeMapper.toDto(customerIncome);
    }

    /**
     *  Delete the  customerIncome by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomerIncome : {}", id);
        customerIncomeRepository.delete(id);
        customerIncomeSearchRepository.delete(id);
    }

    /**
     * Search for the customerIncome corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomerIncomeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CustomerIncomes for query {}", query);
        Page<CustomerIncome> result = customerIncomeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(customerIncomeMapper::toDto);
    }
}
