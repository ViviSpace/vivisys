package com.vivi.vivisys.service;

import com.vivi.vivisys.service.dto.CustomerIncomeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing CustomerIncome.
 */
public interface CustomerIncomeService {

    /**
     * Save a customerIncome.
     *
     * @param customerIncomeDTO the entity to save
     * @return the persisted entity
     */
    CustomerIncomeDTO save(CustomerIncomeDTO customerIncomeDTO);

    /**
     *  Get all the customerIncomes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CustomerIncomeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" customerIncome.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CustomerIncomeDTO findOne(Long id);

    /**
     *  Delete the "id" customerIncome.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the customerIncome corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CustomerIncomeDTO> search(String query, Pageable pageable);
}
