package com.vivi.vivisys.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vivi.vivisys.service.ResourceDeployService;
import com.vivi.vivisys.web.rest.util.HeaderUtil;
import com.vivi.vivisys.web.rest.util.PaginationUtil;
import com.vivi.vivisys.service.dto.ResourceDeployDTO;
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
 * REST controller for managing ResourceDeploy.
 */
@RestController
@RequestMapping("/api")
public class ResourceDeployResource {

    private final Logger log = LoggerFactory.getLogger(ResourceDeployResource.class);

    private static final String ENTITY_NAME = "resourceDeploy";

    private final ResourceDeployService resourceDeployService;

    public ResourceDeployResource(ResourceDeployService resourceDeployService) {
        this.resourceDeployService = resourceDeployService;
    }

    /**
     * POST  /resource-deploys : Create a new resourceDeploy.
     *
     * @param resourceDeployDTO the resourceDeployDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new resourceDeployDTO, or with status 400 (Bad Request) if the resourceDeploy has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/resource-deploys")
    @Timed
    public ResponseEntity<ResourceDeployDTO> createResourceDeploy(@Valid @RequestBody ResourceDeployDTO resourceDeployDTO) throws URISyntaxException {
        log.debug("REST request to save ResourceDeploy : {}", resourceDeployDTO);
        if (resourceDeployDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new resourceDeploy cannot already have an ID")).body(null);
        }
        ResourceDeployDTO result = resourceDeployService.save(resourceDeployDTO);
        return ResponseEntity.created(new URI("/api/resource-deploys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /resource-deploys : Updates an existing resourceDeploy.
     *
     * @param resourceDeployDTO the resourceDeployDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated resourceDeployDTO,
     * or with status 400 (Bad Request) if the resourceDeployDTO is not valid,
     * or with status 500 (Internal Server Error) if the resourceDeployDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/resource-deploys")
    @Timed
    public ResponseEntity<ResourceDeployDTO> updateResourceDeploy(@Valid @RequestBody ResourceDeployDTO resourceDeployDTO) throws URISyntaxException {
        log.debug("REST request to update ResourceDeploy : {}", resourceDeployDTO);
        if (resourceDeployDTO.getId() == null) {
            return createResourceDeploy(resourceDeployDTO);
        }
        ResourceDeployDTO result = resourceDeployService.save(resourceDeployDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, resourceDeployDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /resource-deploys : get all the resourceDeploys.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of resourceDeploys in body
     */
    @GetMapping("/resource-deploys")
    @Timed
    public ResponseEntity<List<ResourceDeployDTO>> getAllResourceDeploys(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ResourceDeploys");
        Page<ResourceDeployDTO> page = resourceDeployService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/resource-deploys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /resource-deploys/:id : get the "id" resourceDeploy.
     *
     * @param id the id of the resourceDeployDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the resourceDeployDTO, or with status 404 (Not Found)
     */
    @GetMapping("/resource-deploys/{id}")
    @Timed
    public ResponseEntity<ResourceDeployDTO> getResourceDeploy(@PathVariable Long id) {
        log.debug("REST request to get ResourceDeploy : {}", id);
        ResourceDeployDTO resourceDeployDTO = resourceDeployService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(resourceDeployDTO));
    }

    /**
     * DELETE  /resource-deploys/:id : delete the "id" resourceDeploy.
     *
     * @param id the id of the resourceDeployDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/resource-deploys/{id}")
    @Timed
    public ResponseEntity<Void> deleteResourceDeploy(@PathVariable Long id) {
        log.debug("REST request to delete ResourceDeploy : {}", id);
        resourceDeployService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/resource-deploys?query=:query : search for the resourceDeploy corresponding
     * to the query.
     *
     * @param query the query of the resourceDeploy search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/resource-deploys")
    @Timed
    public ResponseEntity<List<ResourceDeployDTO>> searchResourceDeploys(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ResourceDeploys for query {}", query);
        Page<ResourceDeployDTO> page = resourceDeployService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/resource-deploys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
