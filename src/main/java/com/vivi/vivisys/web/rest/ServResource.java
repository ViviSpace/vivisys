package com.vivi.vivisys.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vivi.vivisys.service.ServService;
import com.vivi.vivisys.web.rest.util.HeaderUtil;
import com.vivi.vivisys.web.rest.util.PaginationUtil;
import com.vivi.vivisys.service.dto.ServDTO;
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
 * REST controller for managing Serv.
 */
@RestController
@RequestMapping("/api")
public class ServResource {

    private final Logger log = LoggerFactory.getLogger(ServResource.class);

    private static final String ENTITY_NAME = "serv";

    private final ServService servService;

    public ServResource(ServService servService) {
        this.servService = servService;
    }

    /**
     * POST  /servs : Create a new serv.
     *
     * @param servDTO the servDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new servDTO, or with status 400 (Bad Request) if the serv has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/servs")
    @Timed
    public ResponseEntity<ServDTO> createServ(@Valid @RequestBody ServDTO servDTO) throws URISyntaxException {
        log.debug("REST request to save Serv : {}", servDTO);
        if (servDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new serv cannot already have an ID")).body(null);
        }
        ServDTO result = servService.save(servDTO);
        return ResponseEntity.created(new URI("/api/servs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /servs : Updates an existing serv.
     *
     * @param servDTO the servDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated servDTO,
     * or with status 400 (Bad Request) if the servDTO is not valid,
     * or with status 500 (Internal Server Error) if the servDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/servs")
    @Timed
    public ResponseEntity<ServDTO> updateServ(@Valid @RequestBody ServDTO servDTO) throws URISyntaxException {
        log.debug("REST request to update Serv : {}", servDTO);
        if (servDTO.getId() == null) {
            return createServ(servDTO);
        }
        ServDTO result = servService.save(servDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, servDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /servs : get all the servs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of servs in body
     */
    @GetMapping("/servs")
    @Timed
    public ResponseEntity<List<ServDTO>> getAllServs(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Servs");
        Page<ServDTO> page = servService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/servs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /servs/:id : get the "id" serv.
     *
     * @param id the id of the servDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the servDTO, or with status 404 (Not Found)
     */
    @GetMapping("/servs/{id}")
    @Timed
    public ResponseEntity<ServDTO> getServ(@PathVariable Long id) {
        log.debug("REST request to get Serv : {}", id);
        ServDTO servDTO = servService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(servDTO));
    }

    /**
     * DELETE  /servs/:id : delete the "id" serv.
     *
     * @param id the id of the servDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/servs/{id}")
    @Timed
    public ResponseEntity<Void> deleteServ(@PathVariable Long id) {
        log.debug("REST request to delete Serv : {}", id);
        servService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/servs?query=:query : search for the serv corresponding
     * to the query.
     *
     * @param query the query of the serv search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/servs")
    @Timed
    public ResponseEntity<List<ServDTO>> searchServs(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Servs for query {}", query);
        Page<ServDTO> page = servService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/servs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
