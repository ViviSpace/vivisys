package com.vivi.vivisys.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vivi.vivisys.service.ServiceProviderService;
import com.vivi.vivisys.web.rest.util.HeaderUtil;
import com.vivi.vivisys.web.rest.util.PaginationUtil;
import com.vivi.vivisys.service.dto.ServiceProviderDTO;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ServiceProvider.
 */
@RestController
@RequestMapping("/api")
public class ServiceProviderResource {

    private final Logger log = LoggerFactory.getLogger(ServiceProviderResource.class);

    private static final String ENTITY_NAME = "serviceProvider";

    private final ServiceProviderService serviceProviderService;

    public ServiceProviderResource(ServiceProviderService serviceProviderService) {
        this.serviceProviderService = serviceProviderService;
    }

    /**
     * POST  /service-providers : Create a new serviceProvider.
     *
     * @param serviceProviderDTO the serviceProviderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceProviderDTO, or with status 400 (Bad Request) if the serviceProvider has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/service-providers")
    @Timed
    public ResponseEntity<ServiceProviderDTO> createServiceProvider(@Valid @RequestBody ServiceProviderDTO serviceProviderDTO) throws URISyntaxException {
        log.debug("REST request to save ServiceProvider : {}", serviceProviderDTO);
        if (serviceProviderDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new serviceProvider cannot already have an ID")).body(null);
        }
        ServiceProviderDTO result = serviceProviderService.save(serviceProviderDTO);
        return ResponseEntity.created(new URI("/api/service-providers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /service-providers : Updates an existing serviceProvider.
     *
     * @param serviceProviderDTO the serviceProviderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceProviderDTO,
     * or with status 400 (Bad Request) if the serviceProviderDTO is not valid,
     * or with status 500 (Internal Server Error) if the serviceProviderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/service-providers")
    @Timed
    public ResponseEntity<ServiceProviderDTO> updateServiceProvider(@Valid @RequestBody ServiceProviderDTO serviceProviderDTO) throws URISyntaxException {
        log.debug("REST request to update ServiceProvider : {}", serviceProviderDTO);
        if (serviceProviderDTO.getId() == null) {
            return createServiceProvider(serviceProviderDTO);
        }
        ServiceProviderDTO result = serviceProviderService.save(serviceProviderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, serviceProviderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /service-providers : get all the serviceProviders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of serviceProviders in body
     */
    @GetMapping("/service-providers")
    @Timed
    public ResponseEntity<List<ServiceProviderDTO>> getAllServiceProviders(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ServiceProviders");
        Page<ServiceProviderDTO> page = serviceProviderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/service-providers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /service-providers/:id : get the "id" serviceProvider.
     *
     * @param id the id of the serviceProviderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceProviderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/service-providers/{id}")
    @Timed
    public ResponseEntity<ServiceProviderDTO> getServiceProvider(@PathVariable Long id) {
        log.debug("REST request to get ServiceProvider : {}", id);
        ServiceProviderDTO serviceProviderDTO = serviceProviderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(serviceProviderDTO));
    }

    /**
     * DELETE  /service-providers/:id : delete the "id" serviceProvider.
     *
     * @param id the id of the serviceProviderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/service-providers/{id}")
    @Timed
    public ResponseEntity<Void> deleteServiceProvider(@PathVariable Long id) {
        log.debug("REST request to delete ServiceProvider : {}", id);
        serviceProviderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/service-providers?query=:query : search for the serviceProvider corresponding
     * to the query.
     *
     * @param query the query of the serviceProvider search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/service-providers")
    @Timed
    public ResponseEntity<List<ServiceProviderDTO>> searchServiceProviders(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ServiceProviders for query {}", query);
        Page<ServiceProviderDTO> page = serviceProviderService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/service-providers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
