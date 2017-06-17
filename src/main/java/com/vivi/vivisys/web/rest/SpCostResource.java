package com.vivi.vivisys.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vivi.vivisys.service.SpCostService;
import com.vivi.vivisys.web.rest.util.HeaderUtil;
import com.vivi.vivisys.web.rest.util.PaginationUtil;
import com.vivi.vivisys.service.dto.SpCostDTO;
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
 * REST controller for managing SpCost.
 */
@RestController
@RequestMapping("/api")
public class SpCostResource {

    private final Logger log = LoggerFactory.getLogger(SpCostResource.class);

    private static final String ENTITY_NAME = "spCost";

    private final SpCostService spCostService;

    public SpCostResource(SpCostService spCostService) {
        this.spCostService = spCostService;
    }

    /**
     * POST  /sp-costs : Create a new spCost.
     *
     * @param spCostDTO the spCostDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new spCostDTO, or with status 400 (Bad Request) if the spCost has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sp-costs")
    @Timed
    public ResponseEntity<SpCostDTO> createSpCost(@RequestBody SpCostDTO spCostDTO) throws URISyntaxException {
        log.debug("REST request to save SpCost : {}", spCostDTO);
        if (spCostDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new spCost cannot already have an ID")).body(null);
        }
        SpCostDTO result = spCostService.save(spCostDTO);
        return ResponseEntity.created(new URI("/api/sp-costs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sp-costs : Updates an existing spCost.
     *
     * @param spCostDTO the spCostDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated spCostDTO,
     * or with status 400 (Bad Request) if the spCostDTO is not valid,
     * or with status 500 (Internal Server Error) if the spCostDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sp-costs")
    @Timed
    public ResponseEntity<SpCostDTO> updateSpCost(@RequestBody SpCostDTO spCostDTO) throws URISyntaxException {
        log.debug("REST request to update SpCost : {}", spCostDTO);
        if (spCostDTO.getId() == null) {
            return createSpCost(spCostDTO);
        }
        SpCostDTO result = spCostService.save(spCostDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, spCostDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sp-costs : get all the spCosts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of spCosts in body
     */
    @GetMapping("/sp-costs")
    @Timed
    public ResponseEntity<List<SpCostDTO>> getAllSpCosts(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of SpCosts");
        Page<SpCostDTO> page = spCostService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sp-costs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sp-costs/:id : get the "id" spCost.
     *
     * @param id the id of the spCostDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the spCostDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sp-costs/{id}")
    @Timed
    public ResponseEntity<SpCostDTO> getSpCost(@PathVariable Long id) {
        log.debug("REST request to get SpCost : {}", id);
        SpCostDTO spCostDTO = spCostService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(spCostDTO));
    }

    /**
     * DELETE  /sp-costs/:id : delete the "id" spCost.
     *
     * @param id the id of the spCostDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sp-costs/{id}")
    @Timed
    public ResponseEntity<Void> deleteSpCost(@PathVariable Long id) {
        log.debug("REST request to delete SpCost : {}", id);
        spCostService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sp-costs?query=:query : search for the spCost corresponding
     * to the query.
     *
     * @param query the query of the spCost search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/sp-costs")
    @Timed
    public ResponseEntity<List<SpCostDTO>> searchSpCosts(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of SpCosts for query {}", query);
        Page<SpCostDTO> page = spCostService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/sp-costs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
