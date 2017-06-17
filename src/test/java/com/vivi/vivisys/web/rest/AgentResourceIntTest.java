package com.vivi.vivisys.web.rest;

import com.vivi.vivisys.VivisysApp;

import com.vivi.vivisys.domain.Agent;
import com.vivi.vivisys.repository.AgentRepository;
import com.vivi.vivisys.service.AgentService;
import com.vivi.vivisys.repository.search.AgentSearchRepository;
import com.vivi.vivisys.service.dto.AgentDTO;
import com.vivi.vivisys.service.mapper.AgentMapper;
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
 * Test class for the AgentResource REST controller.
 *
 * @see AgentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VivisysApp.class)
public class AgentResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private AgentMapper agentMapper;

    @Autowired
    private AgentService agentService;

    @Autowired
    private AgentSearchRepository agentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAgentMockMvc;

    private Agent agent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AgentResource agentResource = new AgentResource(agentService);
        this.restAgentMockMvc = MockMvcBuilders.standaloneSetup(agentResource)
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
    public static Agent createEntity(EntityManager em) {
        Agent agent = new Agent()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS);
        return agent;
    }

    @Before
    public void initTest() {
        agentSearchRepository.deleteAll();
        agent = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgent() throws Exception {
        int databaseSizeBeforeCreate = agentRepository.findAll().size();

        // Create the Agent
        AgentDTO agentDTO = agentMapper.toDto(agent);
        restAgentMockMvc.perform(post("/api/agents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentDTO)))
            .andExpect(status().isCreated());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeCreate + 1);
        Agent testAgent = agentList.get(agentList.size() - 1);
        assertThat(testAgent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAgent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAgent.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAgent.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Agent in Elasticsearch
        Agent agentEs = agentSearchRepository.findOne(testAgent.getId());
        assertThat(agentEs).isEqualToComparingFieldByField(testAgent);
    }

    @Test
    @Transactional
    public void createAgentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = agentRepository.findAll().size();

        // Create the Agent with an existing ID
        agent.setId(1L);
        AgentDTO agentDTO = agentMapper.toDto(agent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgentMockMvc.perform(post("/api/agents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = agentRepository.findAll().size();
        // set the field null
        agent.setName(null);

        // Create the Agent, which fails.
        AgentDTO agentDTO = agentMapper.toDto(agent);

        restAgentMockMvc.perform(post("/api/agents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentDTO)))
            .andExpect(status().isBadRequest());

        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAgents() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList
        restAgentMockMvc.perform(get("/api/agents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getAgent() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get the agent
        restAgentMockMvc.perform(get("/api/agents/{id}", agent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(agent.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAgent() throws Exception {
        // Get the agent
        restAgentMockMvc.perform(get("/api/agents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgent() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);
        agentSearchRepository.save(agent);
        int databaseSizeBeforeUpdate = agentRepository.findAll().size();

        // Update the agent
        Agent updatedAgent = agentRepository.findOne(agent.getId());
        updatedAgent
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS);
        AgentDTO agentDTO = agentMapper.toDto(updatedAgent);

        restAgentMockMvc.perform(put("/api/agents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentDTO)))
            .andExpect(status().isOk());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
        Agent testAgent = agentList.get(agentList.size() - 1);
        assertThat(testAgent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAgent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAgent.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAgent.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Agent in Elasticsearch
        Agent agentEs = agentSearchRepository.findOne(testAgent.getId());
        assertThat(agentEs).isEqualToComparingFieldByField(testAgent);
    }

    @Test
    @Transactional
    public void updateNonExistingAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().size();

        // Create the Agent
        AgentDTO agentDTO = agentMapper.toDto(agent);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAgentMockMvc.perform(put("/api/agents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentDTO)))
            .andExpect(status().isCreated());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAgent() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);
        agentSearchRepository.save(agent);
        int databaseSizeBeforeDelete = agentRepository.findAll().size();

        // Get the agent
        restAgentMockMvc.perform(delete("/api/agents/{id}", agent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean agentExistsInEs = agentSearchRepository.exists(agent.getId());
        assertThat(agentExistsInEs).isFalse();

        // Validate the database is empty
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAgent() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);
        agentSearchRepository.save(agent);

        // Search the agent
        restAgentMockMvc.perform(get("/api/_search/agents?query=id:" + agent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agent.class);
        Agent agent1 = new Agent();
        agent1.setId(1L);
        Agent agent2 = new Agent();
        agent2.setId(agent1.getId());
        assertThat(agent1).isEqualTo(agent2);
        agent2.setId(2L);
        assertThat(agent1).isNotEqualTo(agent2);
        agent1.setId(null);
        assertThat(agent1).isNotEqualTo(agent2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentDTO.class);
        AgentDTO agentDTO1 = new AgentDTO();
        agentDTO1.setId(1L);
        AgentDTO agentDTO2 = new AgentDTO();
        assertThat(agentDTO1).isNotEqualTo(agentDTO2);
        agentDTO2.setId(agentDTO1.getId());
        assertThat(agentDTO1).isEqualTo(agentDTO2);
        agentDTO2.setId(2L);
        assertThat(agentDTO1).isNotEqualTo(agentDTO2);
        agentDTO1.setId(null);
        assertThat(agentDTO1).isNotEqualTo(agentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(agentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(agentMapper.fromId(null)).isNull();
    }
}
