package com.vivi.vivisys.web.rest;

import com.vivi.vivisys.VivisysApp;

import com.vivi.vivisys.domain.ResourceDeploy;
import com.vivi.vivisys.repository.ResourceDeployRepository;
import com.vivi.vivisys.service.ResourceDeployService;
import com.vivi.vivisys.repository.search.ResourceDeploySearchRepository;
import com.vivi.vivisys.service.dto.ResourceDeployDTO;
import com.vivi.vivisys.service.mapper.ResourceDeployMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.vivi.vivisys.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ResourceDeployResource REST controller.
 *
 * @see ResourceDeployResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VivisysApp.class)
public class ResourceDeployResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_QUANTITY = 1D;
    private static final Double UPDATED_QUANTITY = 2D;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_EFFICTIVE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EFFICTIVE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_EXPRIED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPRIED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private ResourceDeployRepository resourceDeployRepository;

    @Autowired
    private ResourceDeployMapper resourceDeployMapper;

    @Autowired
    private ResourceDeployService resourceDeployService;

    @Autowired
    private ResourceDeploySearchRepository resourceDeploySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restResourceDeployMockMvc;

    private ResourceDeploy resourceDeploy;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ResourceDeployResource resourceDeployResource = new ResourceDeployResource(resourceDeployService);
        this.restResourceDeployMockMvc = MockMvcBuilders.standaloneSetup(resourceDeployResource)
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
    public static ResourceDeploy createEntity(EntityManager em) {
        ResourceDeploy resourceDeploy = new ResourceDeploy()
            .name(DEFAULT_NAME)
            .quantity(DEFAULT_QUANTITY)
            .price(DEFAULT_PRICE)
            .createdTime(DEFAULT_CREATED_TIME)
            .effictiveTime(DEFAULT_EFFICTIVE_TIME)
            .expriedTime(DEFAULT_EXPRIED_TIME)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS);
        return resourceDeploy;
    }

    @Before
    public void initTest() {
        resourceDeploySearchRepository.deleteAll();
        resourceDeploy = createEntity(em);
    }

    @Test
    @Transactional
    public void createResourceDeploy() throws Exception {
        int databaseSizeBeforeCreate = resourceDeployRepository.findAll().size();

        // Create the ResourceDeploy
        ResourceDeployDTO resourceDeployDTO = resourceDeployMapper.toDto(resourceDeploy);
        restResourceDeployMockMvc.perform(post("/api/resource-deploys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceDeployDTO)))
            .andExpect(status().isCreated());

        // Validate the ResourceDeploy in the database
        List<ResourceDeploy> resourceDeployList = resourceDeployRepository.findAll();
        assertThat(resourceDeployList).hasSize(databaseSizeBeforeCreate + 1);
        ResourceDeploy testResourceDeploy = resourceDeployList.get(resourceDeployList.size() - 1);
        assertThat(testResourceDeploy.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testResourceDeploy.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testResourceDeploy.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testResourceDeploy.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testResourceDeploy.getEffictiveTime()).isEqualTo(DEFAULT_EFFICTIVE_TIME);
        assertThat(testResourceDeploy.getExpriedTime()).isEqualTo(DEFAULT_EXPRIED_TIME);
        assertThat(testResourceDeploy.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testResourceDeploy.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the ResourceDeploy in Elasticsearch
        ResourceDeploy resourceDeployEs = resourceDeploySearchRepository.findOne(testResourceDeploy.getId());
        assertThat(resourceDeployEs).isEqualToComparingFieldByField(testResourceDeploy);
    }

    @Test
    @Transactional
    public void createResourceDeployWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resourceDeployRepository.findAll().size();

        // Create the ResourceDeploy with an existing ID
        resourceDeploy.setId(1L);
        ResourceDeployDTO resourceDeployDTO = resourceDeployMapper.toDto(resourceDeploy);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceDeployMockMvc.perform(post("/api/resource-deploys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceDeployDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ResourceDeploy> resourceDeployList = resourceDeployRepository.findAll();
        assertThat(resourceDeployList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceDeployRepository.findAll().size();
        // set the field null
        resourceDeploy.setName(null);

        // Create the ResourceDeploy, which fails.
        ResourceDeployDTO resourceDeployDTO = resourceDeployMapper.toDto(resourceDeploy);

        restResourceDeployMockMvc.perform(post("/api/resource-deploys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceDeployDTO)))
            .andExpect(status().isBadRequest());

        List<ResourceDeploy> resourceDeployList = resourceDeployRepository.findAll();
        assertThat(resourceDeployList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceDeployRepository.findAll().size();
        // set the field null
        resourceDeploy.setQuantity(null);

        // Create the ResourceDeploy, which fails.
        ResourceDeployDTO resourceDeployDTO = resourceDeployMapper.toDto(resourceDeploy);

        restResourceDeployMockMvc.perform(post("/api/resource-deploys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceDeployDTO)))
            .andExpect(status().isBadRequest());

        List<ResourceDeploy> resourceDeployList = resourceDeployRepository.findAll();
        assertThat(resourceDeployList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceDeployRepository.findAll().size();
        // set the field null
        resourceDeploy.setPrice(null);

        // Create the ResourceDeploy, which fails.
        ResourceDeployDTO resourceDeployDTO = resourceDeployMapper.toDto(resourceDeploy);

        restResourceDeployMockMvc.perform(post("/api/resource-deploys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceDeployDTO)))
            .andExpect(status().isBadRequest());

        List<ResourceDeploy> resourceDeployList = resourceDeployRepository.findAll();
        assertThat(resourceDeployList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllResourceDeploys() throws Exception {
        // Initialize the database
        resourceDeployRepository.saveAndFlush(resourceDeploy);

        // Get all the resourceDeployList
        restResourceDeployMockMvc.perform(get("/api/resource-deploys?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceDeploy.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].effictiveTime").value(hasItem(sameInstant(DEFAULT_EFFICTIVE_TIME))))
            .andExpect(jsonPath("$.[*].expriedTime").value(hasItem(sameInstant(DEFAULT_EXPRIED_TIME))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getResourceDeploy() throws Exception {
        // Initialize the database
        resourceDeployRepository.saveAndFlush(resourceDeploy);

        // Get the resourceDeploy
        restResourceDeployMockMvc.perform(get("/api/resource-deploys/{id}", resourceDeploy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resourceDeploy.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.effictiveTime").value(sameInstant(DEFAULT_EFFICTIVE_TIME)))
            .andExpect(jsonPath("$.expriedTime").value(sameInstant(DEFAULT_EXPRIED_TIME)))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingResourceDeploy() throws Exception {
        // Get the resourceDeploy
        restResourceDeployMockMvc.perform(get("/api/resource-deploys/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResourceDeploy() throws Exception {
        // Initialize the database
        resourceDeployRepository.saveAndFlush(resourceDeploy);
        resourceDeploySearchRepository.save(resourceDeploy);
        int databaseSizeBeforeUpdate = resourceDeployRepository.findAll().size();

        // Update the resourceDeploy
        ResourceDeploy updatedResourceDeploy = resourceDeployRepository.findOne(resourceDeploy.getId());
        updatedResourceDeploy
            .name(UPDATED_NAME)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .createdTime(UPDATED_CREATED_TIME)
            .effictiveTime(UPDATED_EFFICTIVE_TIME)
            .expriedTime(UPDATED_EXPRIED_TIME)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS);
        ResourceDeployDTO resourceDeployDTO = resourceDeployMapper.toDto(updatedResourceDeploy);

        restResourceDeployMockMvc.perform(put("/api/resource-deploys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceDeployDTO)))
            .andExpect(status().isOk());

        // Validate the ResourceDeploy in the database
        List<ResourceDeploy> resourceDeployList = resourceDeployRepository.findAll();
        assertThat(resourceDeployList).hasSize(databaseSizeBeforeUpdate);
        ResourceDeploy testResourceDeploy = resourceDeployList.get(resourceDeployList.size() - 1);
        assertThat(testResourceDeploy.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testResourceDeploy.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testResourceDeploy.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testResourceDeploy.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testResourceDeploy.getEffictiveTime()).isEqualTo(UPDATED_EFFICTIVE_TIME);
        assertThat(testResourceDeploy.getExpriedTime()).isEqualTo(UPDATED_EXPRIED_TIME);
        assertThat(testResourceDeploy.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testResourceDeploy.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the ResourceDeploy in Elasticsearch
        ResourceDeploy resourceDeployEs = resourceDeploySearchRepository.findOne(testResourceDeploy.getId());
        assertThat(resourceDeployEs).isEqualToComparingFieldByField(testResourceDeploy);
    }

    @Test
    @Transactional
    public void updateNonExistingResourceDeploy() throws Exception {
        int databaseSizeBeforeUpdate = resourceDeployRepository.findAll().size();

        // Create the ResourceDeploy
        ResourceDeployDTO resourceDeployDTO = resourceDeployMapper.toDto(resourceDeploy);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restResourceDeployMockMvc.perform(put("/api/resource-deploys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceDeployDTO)))
            .andExpect(status().isCreated());

        // Validate the ResourceDeploy in the database
        List<ResourceDeploy> resourceDeployList = resourceDeployRepository.findAll();
        assertThat(resourceDeployList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteResourceDeploy() throws Exception {
        // Initialize the database
        resourceDeployRepository.saveAndFlush(resourceDeploy);
        resourceDeploySearchRepository.save(resourceDeploy);
        int databaseSizeBeforeDelete = resourceDeployRepository.findAll().size();

        // Get the resourceDeploy
        restResourceDeployMockMvc.perform(delete("/api/resource-deploys/{id}", resourceDeploy.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean resourceDeployExistsInEs = resourceDeploySearchRepository.exists(resourceDeploy.getId());
        assertThat(resourceDeployExistsInEs).isFalse();

        // Validate the database is empty
        List<ResourceDeploy> resourceDeployList = resourceDeployRepository.findAll();
        assertThat(resourceDeployList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchResourceDeploy() throws Exception {
        // Initialize the database
        resourceDeployRepository.saveAndFlush(resourceDeploy);
        resourceDeploySearchRepository.save(resourceDeploy);

        // Search the resourceDeploy
        restResourceDeployMockMvc.perform(get("/api/_search/resource-deploys?query=id:" + resourceDeploy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceDeploy.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].effictiveTime").value(hasItem(sameInstant(DEFAULT_EFFICTIVE_TIME))))
            .andExpect(jsonPath("$.[*].expriedTime").value(hasItem(sameInstant(DEFAULT_EXPRIED_TIME))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceDeploy.class);
        ResourceDeploy resourceDeploy1 = new ResourceDeploy();
        resourceDeploy1.setId(1L);
        ResourceDeploy resourceDeploy2 = new ResourceDeploy();
        resourceDeploy2.setId(resourceDeploy1.getId());
        assertThat(resourceDeploy1).isEqualTo(resourceDeploy2);
        resourceDeploy2.setId(2L);
        assertThat(resourceDeploy1).isNotEqualTo(resourceDeploy2);
        resourceDeploy1.setId(null);
        assertThat(resourceDeploy1).isNotEqualTo(resourceDeploy2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceDeployDTO.class);
        ResourceDeployDTO resourceDeployDTO1 = new ResourceDeployDTO();
        resourceDeployDTO1.setId(1L);
        ResourceDeployDTO resourceDeployDTO2 = new ResourceDeployDTO();
        assertThat(resourceDeployDTO1).isNotEqualTo(resourceDeployDTO2);
        resourceDeployDTO2.setId(resourceDeployDTO1.getId());
        assertThat(resourceDeployDTO1).isEqualTo(resourceDeployDTO2);
        resourceDeployDTO2.setId(2L);
        assertThat(resourceDeployDTO1).isNotEqualTo(resourceDeployDTO2);
        resourceDeployDTO1.setId(null);
        assertThat(resourceDeployDTO1).isNotEqualTo(resourceDeployDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(resourceDeployMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(resourceDeployMapper.fromId(null)).isNull();
    }
}
