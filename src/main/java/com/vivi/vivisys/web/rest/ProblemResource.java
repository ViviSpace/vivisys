package com.vivi.vivisys.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vivi.vivisys.service.ProblemService;
import com.vivi.vivisys.web.rest.util.HeaderUtil;
import com.vivi.vivisys.web.rest.util.PaginationUtil;
import com.vivi.vivisys.service.dto.ProblemDTO;
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
 * REST controller for managing Problem.
 */
@RestController
@RequestMapping("/api")
public class ProblemResource {

    private final Logger log = LoggerFactory.getLogger(ProblemResource.class);

    private static final String ENTITY_NAME = "problem";

    private final ProblemService problemService;

    public ProblemResource(ProblemService problemService) {
        this.problemService = problemService;
    }

    /**
     * POST  /problems : Create a new problem.
     *
     * @param problemDTO the problemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new problemDTO, or with status 400 (Bad Request) if the problem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/problems")
    @Timed
    public ResponseEntity<ProblemDTO> createProblem(@RequestBody ProblemDTO problemDTO) throws URISyntaxException {
        log.debug("REST request to save Problem : {}", problemDTO);
        if (problemDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new problem cannot already have an ID")).body(null);
        }
        ProblemDTO result = problemService.save(problemDTO);
        return ResponseEntity.created(new URI("/api/problems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /problems : Updates an existing problem.
     *
     * @param problemDTO the problemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated problemDTO,
     * or with status 400 (Bad Request) if the problemDTO is not valid,
     * or with status 500 (Internal Server Error) if the problemDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/problems")
    @Timed
    public ResponseEntity<ProblemDTO> updateProblem(@RequestBody ProblemDTO problemDTO) throws URISyntaxException {
        log.debug("REST request to update Problem : {}", problemDTO);
        if (problemDTO.getId() == null) {
            return createProblem(problemDTO);
        }
        ProblemDTO result = problemService.save(problemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, problemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /problems : get all the problems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of problems in body
     */
    @GetMapping("/problems")
    @Timed
    public ResponseEntity<List<ProblemDTO>> getAllProblems(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Problems");
        Page<ProblemDTO> page = problemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/problems");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /problems/:id : get the "id" problem.
     *
     * @param id the id of the problemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the problemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/problems/{id}")
    @Timed
    public ResponseEntity<ProblemDTO> getProblem(@PathVariable Long id) {
        log.debug("REST request to get Problem : {}", id);
        ProblemDTO problemDTO = problemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(problemDTO));
    }

    /**
     * DELETE  /problems/:id : delete the "id" problem.
     *
     * @param id the id of the problemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/problems/{id}")
    @Timed
    public ResponseEntity<Void> deleteProblem(@PathVariable Long id) {
        log.debug("REST request to delete Problem : {}", id);
        problemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/problems?query=:query : search for the problem corresponding
     * to the query.
     *
     * @param query the query of the problem search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/problems")
    @Timed
    public ResponseEntity<List<ProblemDTO>> searchProblems(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Problems for query {}", query);
        Page<ProblemDTO> page = problemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/problems");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
