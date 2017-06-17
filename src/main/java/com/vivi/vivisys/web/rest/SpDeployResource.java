package com.vivi.vivisys.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vivi.vivisys.service.SpDeployService;
import com.vivi.vivisys.web.rest.util.HeaderUtil;
import com.vivi.vivisys.web.rest.util.PaginationUtil;
import com.vivi.vivisys.service.dto.SpDeployDTO;
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
 * REST controller for managing SpDeploy.
 */
@RestController
@RequestMapping("/api")
public class SpDeployResource {

    private final Logger log = LoggerFactory.getLogger(SpDeployResource.class);

    private static final String ENTITY_NAME = "spDeploy";

    private final SpDeployService spDeployService;

    public SpDeployResource(SpDeployService spDeployService) {
        this.spDeployService = spDeployService;
    }

    /**
     * POST  /sp-deploys : Create a new spDeploy.
     *
     * @param spDeployDTO the spDeployDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new spDeployDTO, or with status 400 (Bad Request) if the spDeploy has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sp-deploys")
    @Timed
    public ResponseEntity<SpDeployDTO> createSpDeploy(@Valid @RequestBody SpDeployDTO spDeployDTO) throws URISyntaxException {
        log.debug("REST request to save SpDeploy : {}", spDeployDTO);
        if (spDeployDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new spDeploy cannot already have an ID")).body(null);
        }
        SpDeployDTO result = spDeployService.save(spDeployDTO);
        return ResponseEntity.created(new URI("/api/sp-deploys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sp-deploys : Updates an existing spDeploy.
     *
     * @param spDeployDTO the spDeployDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated spDeployDTO,
     * or with status 400 (Bad Request) if the spDeployDTO is not valid,
     * or with status 500 (Internal Server Error) if the spDeployDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sp-deploys")
    @Timed
    public ResponseEntity<SpDeployDTO> updateSpDeploy(@Valid @RequestBody SpDeployDTO spDeployDTO) throws URISyntaxException {
        log.debug("REST request to update SpDeploy : {}", spDeployDTO);
        if (spDeployDTO.getId() == null) {
            return createSpDeploy(spDeployDTO);
        }
        SpDeployDTO result = spDeployService.save(spDeployDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, spDeployDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sp-deploys : get all the spDeploys.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of spDeploys in body
     */
    @GetMapping("/sp-deploys")
    @Timed
    public ResponseEntity<List<SpDeployDTO>> getAllSpDeploys(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of SpDeploys");
        Page<SpDeployDTO> page = spDeployService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sp-deploys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sp-deploys/:id : get the "id" spDeploy.
     *
     * @param id the id of the spDeployDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the spDeployDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sp-deploys/{id}")
    @Timed
    public ResponseEntity<SpDeployDTO> getSpDeploy(@PathVariable Long id) {
        log.debug("REST request to get SpDeploy : {}", id);
        SpDeployDTO spDeployDTO = spDeployService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(spDeployDTO));
    }

    /**
     * DELETE  /sp-deploys/:id : delete the "id" spDeploy.
     *
     * @param id the id of the spDeployDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sp-deploys/{id}")
    @Timed
    public ResponseEntity<Void> deleteSpDeploy(@PathVariable Long id) {
        log.debug("REST request to delete SpDeploy : {}", id);
        spDeployService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sp-deploys?query=:query : search for the spDeploy corresponding
     * to the query.
     *
     * @param query the query of the spDeploy search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/sp-deploys")
    @Timed
    public ResponseEntity<List<SpDeployDTO>> searchSpDeploys(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of SpDeploys for query {}", query);
        Page<SpDeployDTO> page = spDeployService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/sp-deploys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
