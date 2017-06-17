package com.vivi.vivisys.web.rest;

import com.vivi.vivisys.VivisysApp;

import com.vivi.vivisys.domain.ResourceCost;
import com.vivi.vivisys.repository.ResourceCostRepository;
import com.vivi.vivisys.service.ResourceCostService;
import com.vivi.vivisys.repository.search.ResourceCostSearchRepository;
import com.vivi.vivisys.service.dto.ResourceCostDTO;
import com.vivi.vivisys.service.mapper.ResourceCostMapper;
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
 * Test class for the ResourceCostResource REST controller.
 *
 * @see ResourceCostResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VivisysApp.class)
public class ResourceCostResourceIntTest {

    @Autowired
    private ResourceCostRepository resourceCostRepository;

    @Autowired
    private ResourceCostMapper resourceCostMapper;

    @Autowired
    private ResourceCostService resourceCostService;

    @Autowired
    private ResourceCostSearchRepository resourceCostSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restResourceCostMockMvc;

    private ResourceCost resourceCost;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ResourceCostResource resourceCostResource = new ResourceCostResource(resourceCostService);
        this.restResourceCostMockMvc = MockMvcBuilders.standaloneSetup(resourceCostResource)
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
    public static ResourceCost createEntity(EntityManager em) {
        ResourceCost resourceCost = new ResourceCost();
        return resourceCost;
    }

    @Before
    public void initTest() {
        resourceCostSearchRepository.deleteAll();
        resourceCost = createEntity(em);
    }

    @Test
    @Transactional
    public void createResourceCost() throws Exception {
        int databaseSizeBeforeCreate = resourceCostRepository.findAll().size();

        // Create the ResourceCost
        ResourceCostDTO resourceCostDTO = resourceCostMapper.toDto(resourceCost);
        restResourceCostMockMvc.perform(post("/api/resource-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceCostDTO)))
            .andExpect(status().isCreated());

        // Validate the ResourceCost in the database
        List<ResourceCost> resourceCostList = resourceCostRepository.findAll();
        assertThat(resourceCostList).hasSize(databaseSizeBeforeCreate + 1);
        ResourceCost testResourceCost = resourceCostList.get(resourceCostList.size() - 1);

        // Validate the ResourceCost in Elasticsearch
        ResourceCost resourceCostEs = resourceCostSearchRepository.findOne(testResourceCost.getId());
        assertThat(resourceCostEs).isEqualToComparingFieldByField(testResourceCost);
    }

    @Test
    @Transactional
    public void createResourceCostWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resourceCostRepository.findAll().size();

        // Create the ResourceCost with an existing ID
        resourceCost.setId(1L);
        ResourceCostDTO resourceCostDTO = resourceCostMapper.toDto(resourceCost);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceCostMockMvc.perform(post("/api/resource-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceCostDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ResourceCost> resourceCostList = resourceCostRepository.findAll();
        assertThat(resourceCostList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllResourceCosts() throws Exception {
        // Initialize the database
        resourceCostRepository.saveAndFlush(resourceCost);

        // Get all the resourceCostList
        restResourceCostMockMvc.perform(get("/api/resource-costs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceCost.getId().intValue())));
    }

    @Test
    @Transactional
    public void getResourceCost() throws Exception {
        // Initialize the database
        resourceCostRepository.saveAndFlush(resourceCost);

        // Get the resourceCost
        restResourceCostMockMvc.perform(get("/api/resource-costs/{id}", resourceCost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resourceCost.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingResourceCost() throws Exception {
        // Get the resourceCost
        restResourceCostMockMvc.perform(get("/api/resource-costs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResourceCost() throws Exception {
        // Initialize the database
        resourceCostRepository.saveAndFlush(resourceCost);
        resourceCostSearchRepository.save(resourceCost);
        int databaseSizeBeforeUpdate = resourceCostRepository.findAll().size();

        // Update the resourceCost
        ResourceCost updatedResourceCost = resourceCostRepository.findOne(resourceCost.getId());
        ResourceCostDTO resourceCostDTO = resourceCostMapper.toDto(updatedResourceCost);

        restResourceCostMockMvc.perform(put("/api/resource-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceCostDTO)))
            .andExpect(status().isOk());

        // Validate the ResourceCost in the database
        List<ResourceCost> resourceCostList = resourceCostRepository.findAll();
        assertThat(resourceCostList).hasSize(databaseSizeBeforeUpdate);
        ResourceCost testResourceCost = resourceCostList.get(resourceCostList.size() - 1);

        // Validate the ResourceCost in Elasticsearch
        ResourceCost resourceCostEs = resourceCostSearchRepository.findOne(testResourceCost.getId());
        assertThat(resourceCostEs).isEqualToComparingFieldByField(testResourceCost);
    }

    @Test
    @Transactional
    public void updateNonExistingResourceCost() throws Exception {
        int databaseSizeBeforeUpdate = resourceCostRepository.findAll().size();

        // Create the ResourceCost
        ResourceCostDTO resourceCostDTO = resourceCostMapper.toDto(resourceCost);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restResourceCostMockMvc.perform(put("/api/resource-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceCostDTO)))
            .andExpect(status().isCreated());

        // Validate the ResourceCost in the database
        List<ResourceCost> resourceCostList = resourceCostRepository.findAll();
        assertThat(resourceCostList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteResourceCost() throws Exception {
        // Initialize the database
        resourceCostRepository.saveAndFlush(resourceCost);
        resourceCostSearchRepository.save(resourceCost);
        int databaseSizeBeforeDelete = resourceCostRepository.findAll().size();

        // Get the resourceCost
        restResourceCostMockMvc.perform(delete("/api/resource-costs/{id}", resourceCost.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean resourceCostExistsInEs = resourceCostSearchRepository.exists(resourceCost.getId());
        assertThat(resourceCostExistsInEs).isFalse();

        // Validate the database is empty
        List<ResourceCost> resourceCostList = resourceCostRepository.findAll();
        assertThat(resourceCostList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchResourceCost() throws Exception {
        // Initialize the database
        resourceCostRepository.saveAndFlush(resourceCost);
        resourceCostSearchRepository.save(resourceCost);

        // Search the resourceCost
        restResourceCostMockMvc.perform(get("/api/_search/resource-costs?query=id:" + resourceCost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceCost.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceCost.class);
        ResourceCost resourceCost1 = new ResourceCost();
        resourceCost1.setId(1L);
        ResourceCost resourceCost2 = new ResourceCost();
        resourceCost2.setId(resourceCost1.getId());
        assertThat(resourceCost1).isEqualTo(resourceCost2);
        resourceCost2.setId(2L);
        assertThat(resourceCost1).isNotEqualTo(resourceCost2);
        resourceCost1.setId(null);
        assertThat(resourceCost1).isNotEqualTo(resourceCost2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceCostDTO.class);
        ResourceCostDTO resourceCostDTO1 = new ResourceCostDTO();
        resourceCostDTO1.setId(1L);
        ResourceCostDTO resourceCostDTO2 = new ResourceCostDTO();
        assertThat(resourceCostDTO1).isNotEqualTo(resourceCostDTO2);
        resourceCostDTO2.setId(resourceCostDTO1.getId());
        assertThat(resourceCostDTO1).isEqualTo(resourceCostDTO2);
        resourceCostDTO2.setId(2L);
        assertThat(resourceCostDTO1).isNotEqualTo(resourceCostDTO2);
        resourceCostDTO1.setId(null);
        assertThat(resourceCostDTO1).isNotEqualTo(resourceCostDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(resourceCostMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(resourceCostMapper.fromId(null)).isNull();
    }
}
