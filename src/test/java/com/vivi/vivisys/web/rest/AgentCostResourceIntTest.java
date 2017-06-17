package com.vivi.vivisys.web.rest;

import com.vivi.vivisys.VivisysApp;

import com.vivi.vivisys.domain.AgentCost;
import com.vivi.vivisys.repository.AgentCostRepository;
import com.vivi.vivisys.service.AgentCostService;
import com.vivi.vivisys.repository.search.AgentCostSearchRepository;
import com.vivi.vivisys.service.dto.AgentCostDTO;
import com.vivi.vivisys.service.mapper.AgentCostMapper;
import com.vivi.vivisys.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AgentCostResource REST controller.
 *
 * @see AgentCostResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VivisysApp.class)
public class AgentCostResourceIntTest {

    @Autowired
    private AgentCostRepository agentCostRepository;

    @Autowired
    private AgentCostMapper agentCostMapper;

    @Autowired
    private AgentCostService agentCostService;

    @Autowired
    private AgentCostSearchRepository agentCostSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAgentCostMockMvc;

    private AgentCost agentCost;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AgentCostResource agentCostResource = new AgentCostResource(agentCostService);
        this.restAgentCostMockMvc = MockMvcBuilders.standaloneSetup(agentCostResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgentCost createEntity(EntityManager em) {
        AgentCost agentCost = new AgentCost();
        return agentCost;
    }

    @Before
    public void initTest() {
        agentCostSearchRepository.deleteAll();
        agentCost = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgentCost() throws Exception {
        int databaseSizeBeforeCreate = agentCostRepository.findAll().size();

        // Create the AgentCost
        AgentCostDTO agentCostDTO = agentCostMapper.toDto(agentCost);
        restAgentCostMockMvc.perform(post("/api/agent-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentCostDTO)))
            .andExpect(status().isCreated());

        // Validate the AgentCost in the database
        List<AgentCost> agentCostList = agentCostRepository.findAll();
        assertThat(agentCostList).hasSize(databaseSizeBeforeCreate + 1);
        AgentCost testAgentCost = agentCostList.get(agentCostList.size() - 1);

        // Validate the AgentCost in Elasticsearch
        AgentCost agentCostEs = agentCostSearchRepository.findOne(testAgentCost.getId());
        assertThat(agentCostEs).isEqualToComparingFieldByField(testAgentCost);
    }

    @Test
    @Transactional
    public void createAgentCostWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = agentCostRepository.findAll().size();

        // Create the AgentCost with an existing ID
        agentCost.setId(1L);
        AgentCostDTO agentCostDTO = agentCostMapper.toDto(agentCost);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgentCostMockMvc.perform(post("/api/agent-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentCostDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AgentCost> agentCostList = agentCostRepository.findAll();
        assertThat(agentCostList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAgentCosts() throws Exception {
        // Initialize the database
        agentCostRepository.saveAndFlush(agentCost);

        // Get all the agentCostList
        restAgentCostMockMvc.perform(get("/api/agent-costs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agentCost.getId().intValue())));
    }

    @Test
    @Transactional
    public void getAgentCost() throws Exception {
        // Initialize the database
        agentCostRepository.saveAndFlush(agentCost);

        // Get the agentCost
        restAgentCostMockMvc.perform(get("/api/agent-costs/{id}", agentCost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(agentCost.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAgentCost() throws Exception {
        // Get the agentCost
        restAgentCostMockMvc.perform(get("/api/agent-costs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgentCost() throws Exception {
        // Initialize the database
        agentCostRepository.saveAndFlush(agentCost);
        agentCostSearchRepository.save(agentCost);
        int databaseSizeBeforeUpdate = agentCostRepository.findAll().size();

        // Update the agentCost
        AgentCost updatedAgentCost = agentCostRepository.findOne(agentCost.getId());
        AgentCostDTO agentCostDTO = agentCostMapper.toDto(updatedAgentCost);

        restAgentCostMockMvc.perform(put("/api/agent-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentCostDTO)))
            .andExpect(status().isOk());

        // Validate the AgentCost in the database
        List<AgentCost> agentCostList = agentCostRepository.findAll();
        assertThat(agentCostList).hasSize(databaseSizeBeforeUpdate);
        AgentCost testAgentCost = agentCostList.get(agentCostList.size() - 1);

        // Validate the AgentCost in Elasticsearch
        AgentCost agentCostEs = agentCostSearchRepository.findOne(testAgentCost.getId());
        assertThat(agentCostEs).isEqualToComparingFieldByField(testAgentCost);
    }

    @Test
    @Transactional
    public void updateNonExistingAgentCost() throws Exception {
        int databaseSizeBeforeUpdate = agentCostRepository.findAll().size();

        // Create the AgentCost
        AgentCostDTO agentCostDTO = agentCostMapper.toDto(agentCost);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAgentCostMockMvc.perform(put("/api/agent-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentCostDTO)))
            .andExpect(status().isCreated());

        // Validate the AgentCost in the database
        List<AgentCost> agentCostList = agentCostRepository.findAll();
        assertThat(agentCostList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAgentCost() throws Exception {
        // Initialize the database
        agentCostRepository.saveAndFlush(agentCost);
        agentCostSearchRepository.save(agentCost);
        int databaseSizeBeforeDelete = agentCostRepository.findAll().size();

        // Get the agentCost
        restAgentCostMockMvc.perform(delete("/api/agent-costs/{id}", agentCost.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean agentCostExistsInEs = agentCostSearchRepository.exists(agentCost.getId());
        assertThat(agentCostExistsInEs).isFalse();

        // Validate the database is empty
        List<AgentCost> agentCostList = agentCostRepository.findAll();
        assertThat(agentCostList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAgentCost() throws Exception {
        // Initialize the database
        agentCostRepository.saveAndFlush(agentCost);
        agentCostSearchRepository.save(agentCost);

        // Search the agentCost
        restAgentCostMockMvc.perform(get("/api/_search/agent-costs?query=id:" + agentCost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agentCost.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentCost.class);
        AgentCost agentCost1 = new AgentCost();
        agentCost1.setId(1L);
        AgentCost agentCost2 = new AgentCost();
        agentCost2.setId(agentCost1.getId());
        assertThat(agentCost1).isEqualTo(agentCost2);
        agentCost2.setId(2L);
        assertThat(agentCost1).isNotEqualTo(agentCost2);
        agentCost1.setId(null);
        assertThat(agentCost1).isNotEqualTo(agentCost2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentCostDTO.class);
        AgentCostDTO agentCostDTO1 = new AgentCostDTO();
        agentCostDTO1.setId(1L);
        AgentCostDTO agentCostDTO2 = new AgentCostDTO();
        assertThat(agentCostDTO1).isNotEqualTo(agentCostDTO2);
        agentCostDTO2.setId(agentCostDTO1.getId());
        assertThat(agentCostDTO1).isEqualTo(agentCostDTO2);
        agentCostDTO2.setId(2L);
        assertThat(agentCostDTO1).isNotEqualTo(agentCostDTO2);
        agentCostDTO1.setId(null);
        assertThat(agentCostDTO1).isNotEqualTo(agentCostDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(agentCostMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(agentCostMapper.fromId(null)).isNull();
    }
}
