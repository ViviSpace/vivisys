package com.vivi.vivisys.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vivi.vivisys.service.AgentCostService;
import com.vivi.vivisys.web.rest.util.HeaderUtil;
import com.vivi.vivisys.web.rest.util.PaginationUtil;
import com.vivi.vivisys.service.dto.AgentCostDTO;
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
 * REST controller for managing AgentCost.
 */
@RestController
@RequestMapping("/api")
public class AgentCostResource {

    private final Logger log = LoggerFactory.getLogger(AgentCostResource.class);

    private static final String ENTITY_NAME = "agentCost";

    private final AgentCostService agentCostService;

    public AgentCostResource(AgentCostService agentCostService) {
        this.agentCostService = agentCostService;
    }

    /**
     * POST  /agent-costs : Create a new agentCost.
     *
     * @param agentCostDTO the agentCostDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new agentCostDTO, or with status 400 (Bad Request) if the agentCost has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/agent-costs")
    @Timed
    public ResponseEntity<AgentCostDTO> createAgentCost(@RequestBody AgentCostDTO agentCostDTO) throws URISyntaxException {
        log.debug("REST request to save AgentCost : {}", agentCostDTO);
        if (agentCostDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new agentCost cannot already have an ID")).body(null);
        }
        AgentCostDTO result = agentCostService.save(agentCostDTO);
        return ResponseEntity.created(new URI("/api/agent-costs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /agent-costs : Updates an existing agentCost.
     *
     * @param agentCostDTO the agentCostDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated agentCostDTO,
     * or with status 400 (Bad Request) if the agentCostDTO is not valid,
     * or with status 500 (Internal Server Error) if the agentCostDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/agent-costs")
    @Timed
    public ResponseEntity<AgentCostDTO> updateAgentCost(@RequestBody AgentCostDTO agentCostDTO) throws URISyntaxException {
        log.debug("REST request to update AgentCost : {}", agentCostDTO);
        if (agentCostDTO.getId() == null) {
            return createAgentCost(agentCostDTO);
        }
        AgentCostDTO result = agentCostService.save(agentCostDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, agentCostDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /agent-costs : get all the agentCosts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of agentCosts in body
     */
    @GetMapping("/agent-costs")
    @Timed
    public ResponseEntity<List<AgentCostDTO>> getAllAgentCosts(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of AgentCosts");
        Page<AgentCostDTO> page = agentCostService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/agent-costs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /agent-costs/:id : get the "id" agentCost.
     *
     * @param id the id of the agentCostDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the agentCostDTO, or with status 404 (Not Found)
     */
    @GetMapping("/agent-costs/{id}")
    @Timed
    public ResponseEntity<AgentCostDTO> getAgentCost(@PathVariable Long id) {
        log.debug("REST request to get AgentCost : {}", id);
        AgentCostDTO agentCostDTO = agentCostService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(agentCostDTO));
    }

    /**
     * DELETE  /agent-costs/:id : delete the "id" agentCost.
     *
     * @param id the id of the agentCostDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/agent-costs/{id}")
    @Timed
    public ResponseEntity<Void> deleteAgentCost(@PathVariable Long id) {
        log.debug("REST request to delete AgentCost : {}", id);
        agentCostService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/agent-costs?query=:query : search for the agentCost corresponding
     * to the query.
     *
     * @param query the query of the agentCost search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/agent-costs")
    @Timed
    public ResponseEntity<List<AgentCostDTO>> searchAgentCosts(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of AgentCosts for query {}", query);
        Page<AgentCostDTO> page = agentCostService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/agent-costs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
