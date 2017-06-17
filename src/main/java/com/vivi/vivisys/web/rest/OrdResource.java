package com.vivi.vivisys.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vivi.vivisys.service.OrdService;
import com.vivi.vivisys.web.rest.util.HeaderUtil;
import com.vivi.vivisys.web.rest.util.PaginationUtil;
import com.vivi.vivisys.service.dto.OrdDTO;
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
 * REST controller for managing Ord.
 */
@RestController
@RequestMapping("/api")
public class OrdResource {

    private final Logger log = LoggerFactory.getLogger(OrdResource.class);

    private static final String ENTITY_NAME = "ord";

    private final OrdService ordService;

    public OrdResource(OrdService ordService) {
        this.ordService = ordService;
    }

    /**
     * POST  /ords : Create a new ord.
     *
     * @param ordDTO the ordDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ordDTO, or with status 400 (Bad Request) if the ord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ords")
    @Timed
    public ResponseEntity<OrdDTO> createOrd(@Valid @RequestBody OrdDTO ordDTO) throws URISyntaxException {
        log.debug("REST request to save Ord : {}", ordDTO);
        if (ordDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ord cannot already have an ID")).body(null);
        }
        OrdDTO result = ordService.save(ordDTO);
        return ResponseEntity.created(new URI("/api/ords/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ords : Updates an existing ord.
     *
     * @param ordDTO the ordDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ordDTO,
     * or with status 400 (Bad Request) if the ordDTO is not valid,
     * or with status 500 (Internal Server Error) if the ordDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ords")
    @Timed
    public ResponseEntity<OrdDTO> updateOrd(@Valid @RequestBody OrdDTO ordDTO) throws URISyntaxException {
        log.debug("REST request to update Ord : {}", ordDTO);
        if (ordDTO.getId() == null) {
            return createOrd(ordDTO);
        }
        OrdDTO result = ordService.save(ordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ordDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ords : get all the ords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ords in body
     */
    @GetMapping("/ords")
    @Timed
    public ResponseEntity<List<OrdDTO>> getAllOrds(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Ords");
        Page<OrdDTO> page = ordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ords");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ords/:id : get the "id" ord.
     *
     * @param id the id of the ordDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ordDTO, or with status 404 (Not Found)
     */
    @GetMapping("/ords/{id}")
    @Timed
    public ResponseEntity<OrdDTO> getOrd(@PathVariable Long id) {
        log.debug("REST request to get Ord : {}", id);
        OrdDTO ordDTO = ordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ordDTO));
    }

    /**
     * DELETE  /ords/:id : delete the "id" ord.
     *
     * @param id the id of the ordDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ords/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrd(@PathVariable Long id) {
        log.debug("REST request to delete Ord : {}", id);
        ordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/ords?query=:query : search for the ord corresponding
     * to the query.
     *
     * @param query the query of the ord search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/ords")
    @Timed
    public ResponseEntity<List<OrdDTO>> searchOrds(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Ords for query {}", query);
        Page<OrdDTO> page = ordService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ords");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
