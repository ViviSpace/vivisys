package com.vivi.vivisys.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vivi.vivisys.service.ProblemOrderService;
import com.vivi.vivisys.web.rest.util.HeaderUtil;
import com.vivi.vivisys.web.rest.util.PaginationUtil;
import com.vivi.vivisys.service.dto.ProblemOrderDTO;
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
 * REST controller for managing ProblemOrder.
 */
@RestController
@RequestMapping("/api")
public class ProblemOrderResource {

    private final Logger log = LoggerFactory.getLogger(ProblemOrderResource.class);

    private static final String ENTITY_NAME = "problemOrder";

    private final ProblemOrderService problemOrderService;

    public ProblemOrderResource(ProblemOrderService problemOrderService) {
        this.problemOrderService = problemOrderService;
    }

    /**
     * POST  /problem-orders : Create a new problemOrder.
     *
     * @param problemOrderDTO the problemOrderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new problemOrderDTO, or with status 400 (Bad Request) if the problemOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/problem-orders")
    @Timed
    public ResponseEntity<ProblemOrderDTO> createProblemOrder(@RequestBody ProblemOrderDTO problemOrderDTO) throws URISyntaxException {
        log.debug("REST request to save ProblemOrder : {}", problemOrderDTO);
        if (problemOrderDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new problemOrder cannot already have an ID")).body(null);
        }
        ProblemOrderDTO result = problemOrderService.save(problemOrderDTO);
        return ResponseEntity.created(new URI("/api/problem-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /problem-orders : Updates an existing problemOrder.
     *
     * @param problemOrderDTO the problemOrderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated problemOrderDTO,
     * or with status 400 (Bad Request) if the problemOrderDTO is not valid,
     * or with status 500 (Internal Server Error) if the problemOrderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/problem-orders")
    @Timed
    public ResponseEntity<ProblemOrderDTO> updateProblemOrder(@RequestBody ProblemOrderDTO problemOrderDTO) throws URISyntaxException {
        log.debug("REST request to update ProblemOrder : {}", problemOrderDTO);
        if (problemOrderDTO.getId() == null) {
            return createProblemOrder(problemOrderDTO);
        }
        ProblemOrderDTO result = problemOrderService.save(problemOrderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, problemOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /problem-orders : get all the problemOrders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of problemOrders in body
     */
    @GetMapping("/problem-orders")
    @Timed
    public ResponseEntity<List<ProblemOrderDTO>> getAllProblemOrders(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ProblemOrders");
        Page<ProblemOrderDTO> page = problemOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/problem-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /problem-orders/:id : get the "id" problemOrder.
     *
     * @param id the id of the problemOrderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the problemOrderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/problem-orders/{id}")
    @Timed
    public ResponseEntity<ProblemOrderDTO> getProblemOrder(@PathVariable Long id) {
        log.debug("REST request to get ProblemOrder : {}", id);
        ProblemOrderDTO problemOrderDTO = problemOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(problemOrderDTO));
    }

    /**
     * DELETE  /problem-orders/:id : delete the "id" problemOrder.
     *
     * @param id the id of the problemOrderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/problem-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteProblemOrder(@PathVariable Long id) {
        log.debug("REST request to delete ProblemOrder : {}", id);
        problemOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/problem-orders?query=:query : search for the problemOrder corresponding
     * to the query.
     *
     * @param query the query of the problemOrder search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/problem-orders")
    @Timed
    public ResponseEntity<List<ProblemOrderDTO>> searchProblemOrders(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ProblemOrders for query {}", query);
        Page<ProblemOrderDTO> page = problemOrderService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/problem-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
