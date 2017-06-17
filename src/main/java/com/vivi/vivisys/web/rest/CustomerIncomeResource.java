package com.vivi.vivisys.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vivi.vivisys.service.CustomerIncomeService;
import com.vivi.vivisys.web.rest.util.HeaderUtil;
import com.vivi.vivisys.web.rest.util.PaginationUtil;
import com.vivi.vivisys.service.dto.CustomerIncomeDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CustomerIncome.
 */
@RestController
@RequestMapping("/api")
public class CustomerIncomeResource {

    private final Logger log = LoggerFactory.getLogger(CustomerIncomeResource.class);

    private static final String ENTITY_NAME = "customerIncome";

    private final CustomerIncomeService customerIncomeService;

    public CustomerIncomeResource(CustomerIncomeService customerIncomeService) {
        this.customerIncomeService = customerIncomeService;
    }

    /**
     * POST  /customer-incomes : Create a new customerIncome.
     *
     * @param customerIncomeDTO the customerIncomeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerIncomeDTO, or with status 400 (Bad Request) if the customerIncome has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customer-incomes")
    @Timed
    public ResponseEntity<CustomerIncomeDTO> createCustomerIncome(@RequestBody CustomerIncomeDTO customerIncomeDTO) throws URISyntaxException {
        log.debug("REST request to save CustomerIncome : {}", customerIncomeDTO);
        if (customerIncomeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new customerIncome cannot already have an ID")).body(null);
        }
        CustomerIncomeDTO result = customerIncomeService.save(customerIncomeDTO);
        return ResponseEntity.created(new URI("/api/customer-incomes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customer-incomes : Updates an existing customerIncome.
     *
     * @param customerIncomeDTO the customerIncomeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerIncomeDTO,
     * or with status 400 (Bad Request) if the customerIncomeDTO is not valid,
     * or with status 500 (Internal Server Error) if the customerIncomeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/customer-incomes")
    @Timed
    public ResponseEntity<CustomerIncomeDTO> updateCustomerIncome(@RequestBody CustomerIncomeDTO customerIncomeDTO) throws URISyntaxException {
        log.debug("REST request to update CustomerIncome : {}", customerIncomeDTO);
        if (customerIncomeDTO.getId() == null) {
            return createCustomerIncome(customerIncomeDTO);
        }
        CustomerIncomeDTO result = customerIncomeService.save(customerIncomeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customerIncomeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customer-incomes : get all the customerIncomes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of customerIncomes in body
     */
    @GetMapping("/customer-incomes")
    @Timed
    public ResponseEntity<List<CustomerIncomeDTO>> getAllCustomerIncomes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CustomerIncomes");
        Page<CustomerIncomeDTO> page = customerIncomeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customer-incomes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /customer-incomes/:id : get the "id" customerIncome.
     *
     * @param id the id of the customerIncomeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerIncomeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/customer-incomes/{id}")
    @Timed
    public ResponseEntity<CustomerIncomeDTO> getCustomerIncome(@PathVariable Long id) {
        log.debug("REST request to get CustomerIncome : {}", id);
        CustomerIncomeDTO customerIncomeDTO = customerIncomeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(customerIncomeDTO));
    }

    /**
     * DELETE  /customer-incomes/:id : delete the "id" customerIncome.
     *
     * @param id the id of the customerIncomeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/customer-incomes/{id}")
    @Timed
    public ResponseEntity<Void> deleteCustomerIncome(@PathVariable Long id) {
        log.debug("REST request to delete CustomerIncome : {}", id);
        customerIncomeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/customer-incomes?query=:query : search for the customerIncome corresponding
     * to the query.
     *
     * @param query the query of the customerIncome search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/customer-incomes")
    @Timed
    public ResponseEntity<List<CustomerIncomeDTO>> searchCustomerIncomes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of CustomerIncomes for query {}", query);
        Page<CustomerIncomeDTO> page = customerIncomeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/customer-incomes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
