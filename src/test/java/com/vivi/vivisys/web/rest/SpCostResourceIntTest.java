package com.vivi.vivisys.web.rest;

import com.vivi.vivisys.VivisysApp;

import com.vivi.vivisys.domain.SpCost;
import com.vivi.vivisys.repository.SpCostRepository;
import com.vivi.vivisys.service.SpCostService;
import com.vivi.vivisys.repository.search.SpCostSearchRepository;
import com.vivi.vivisys.service.dto.SpCostDTO;
import com.vivi.vivisys.service.mapper.SpCostMapper;
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
 * Test class for the SpCostResource REST controller.
 *
 * @see SpCostResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VivisysApp.class)
public class SpCostResourceIntTest {

    @Autowired
    private SpCostRepository spCostRepository;

    @Autowired
    private SpCostMapper spCostMapper;

    @Autowired
    private SpCostService spCostService;

    @Autowired
    private SpCostSearchRepository spCostSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSpCostMockMvc;

    private SpCost spCost;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SpCostResource spCostResource = new SpCostResource(spCostService);
        this.restSpCostMockMvc = MockMvcBuilders.standaloneSetup(spCostResource)
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
    public static SpCost createEntity(EntityManager em) {
        SpCost spCost = new SpCost();
        return spCost;
    }

    @Before
    public void initTest() {
        spCostSearchRepository.deleteAll();
        spCost = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpCost() throws Exception {
        int databaseSizeBeforeCreate = spCostRepository.findAll().size();

        // Create the SpCost
        SpCostDTO spCostDTO = spCostMapper.toDto(spCost);
        restSpCostMockMvc.perform(post("/api/sp-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spCostDTO)))
            .andExpect(status().isCreated());

        // Validate the SpCost in the database
        List<SpCost> spCostList = spCostRepository.findAll();
        assertThat(spCostList).hasSize(databaseSizeBeforeCreate + 1);
        SpCost testSpCost = spCostList.get(spCostList.size() - 1);

        // Validate the SpCost in Elasticsearch
        SpCost spCostEs = spCostSearchRepository.findOne(testSpCost.getId());
        assertThat(spCostEs).isEqualToComparingFieldByField(testSpCost);
    }

    @Test
    @Transactional
    public void createSpCostWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = spCostRepository.findAll().size();

        // Create the SpCost with an existing ID
        spCost.setId(1L);
        SpCostDTO spCostDTO = spCostMapper.toDto(spCost);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpCostMockMvc.perform(post("/api/sp-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spCostDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SpCost> spCostList = spCostRepository.findAll();
        assertThat(spCostList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSpCosts() throws Exception {
        // Initialize the database
        spCostRepository.saveAndFlush(spCost);

        // Get all the spCostList
        restSpCostMockMvc.perform(get("/api/sp-costs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spCost.getId().intValue())));
    }

    @Test
    @Transactional
    public void getSpCost() throws Exception {
        // Initialize the database
        spCostRepository.saveAndFlush(spCost);

        // Get the spCost
        restSpCostMockMvc.perform(get("/api/sp-costs/{id}", spCost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(spCost.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSpCost() throws Exception {
        // Get the spCost
        restSpCostMockMvc.perform(get("/api/sp-costs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpCost() throws Exception {
        // Initialize the database
        spCostRepository.saveAndFlush(spCost);
        spCostSearchRepository.save(spCost);
        int databaseSizeBeforeUpdate = spCostRepository.findAll().size();

        // Update the spCost
        SpCost updatedSpCost = spCostRepository.findOne(spCost.getId());
        SpCostDTO spCostDTO = spCostMapper.toDto(updatedSpCost);

        restSpCostMockMvc.perform(put("/api/sp-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spCostDTO)))
            .andExpect(status().isOk());

        // Validate the SpCost in the database
        List<SpCost> spCostList = spCostRepository.findAll();
        assertThat(spCostList).hasSize(databaseSizeBeforeUpdate);
        SpCost testSpCost = spCostList.get(spCostList.size() - 1);

        // Validate the SpCost in Elasticsearch
        SpCost spCostEs = spCostSearchRepository.findOne(testSpCost.getId());
        assertThat(spCostEs).isEqualToComparingFieldByField(testSpCost);
    }

    @Test
    @Transactional
    public void updateNonExistingSpCost() throws Exception {
        int databaseSizeBeforeUpdate = spCostRepository.findAll().size();

        // Create the SpCost
        SpCostDTO spCostDTO = spCostMapper.toDto(spCost);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSpCostMockMvc.perform(put("/api/sp-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spCostDTO)))
            .andExpect(status().isCreated());

        // Validate the SpCost in the database
        List<SpCost> spCostList = spCostRepository.findAll();
        assertThat(spCostList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSpCost() throws Exception {
        // Initialize the database
        spCostRepository.saveAndFlush(spCost);
        spCostSearchRepository.save(spCost);
        int databaseSizeBeforeDelete = spCostRepository.findAll().size();

        // Get the spCost
        restSpCostMockMvc.perform(delete("/api/sp-costs/{id}", spCost.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean spCostExistsInEs = spCostSearchRepository.exists(spCost.getId());
        assertThat(spCostExistsInEs).isFalse();

        // Validate the database is empty
        List<SpCost> spCostList = spCostRepository.findAll();
        assertThat(spCostList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSpCost() throws Exception {
        // Initialize the database
        spCostRepository.saveAndFlush(spCost);
        spCostSearchRepository.save(spCost);

        // Search the spCost
        restSpCostMockMvc.perform(get("/api/_search/sp-costs?query=id:" + spCost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spCost.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpCost.class);
        SpCost spCost1 = new SpCost();
        spCost1.setId(1L);
        SpCost spCost2 = new SpCost();
        spCost2.setId(spCost1.getId());
        assertThat(spCost1).isEqualTo(spCost2);
        spCost2.setId(2L);
        assertThat(spCost1).isNotEqualTo(spCost2);
        spCost1.setId(null);
        assertThat(spCost1).isNotEqualTo(spCost2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpCostDTO.class);
        SpCostDTO spCostDTO1 = new SpCostDTO();
        spCostDTO1.setId(1L);
        SpCostDTO spCostDTO2 = new SpCostDTO();
        assertThat(spCostDTO1).isNotEqualTo(spCostDTO2);
        spCostDTO2.setId(spCostDTO1.getId());
        assertThat(spCostDTO1).isEqualTo(spCostDTO2);
        spCostDTO2.setId(2L);
        assertThat(spCostDTO1).isNotEqualTo(spCostDTO2);
        spCostDTO1.setId(null);
        assertThat(spCostDTO1).isNotEqualTo(spCostDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(spCostMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(spCostMapper.fromId(null)).isNull();
    }
}
