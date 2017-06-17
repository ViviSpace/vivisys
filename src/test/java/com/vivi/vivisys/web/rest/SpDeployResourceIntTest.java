package com.vivi.vivisys.web.rest;

import com.vivi.vivisys.VivisysApp;

import com.vivi.vivisys.domain.SpDeploy;
import com.vivi.vivisys.repository.SpDeployRepository;
import com.vivi.vivisys.service.SpDeployService;
import com.vivi.vivisys.repository.search.SpDeploySearchRepository;
import com.vivi.vivisys.service.dto.SpDeployDTO;
import com.vivi.vivisys.service.mapper.SpDeployMapper;
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
 * Test class for the SpDeployResource REST controller.
 *
 * @see SpDeployResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VivisysApp.class)
public class SpDeployResourceIntTest {

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
    private SpDeployRepository spDeployRepository;

    @Autowired
    private SpDeployMapper spDeployMapper;

    @Autowired
    private SpDeployService spDeployService;

    @Autowired
    private SpDeploySearchRepository spDeploySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSpDeployMockMvc;

    private SpDeploy spDeploy;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SpDeployResource spDeployResource = new SpDeployResource(spDeployService);
        this.restSpDeployMockMvc = MockMvcBuilders.standaloneSetup(spDeployResource)
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
    public static SpDeploy createEntity(EntityManager em) {
        SpDeploy spDeploy = new SpDeploy()
            .name(DEFAULT_NAME)
            .quantity(DEFAULT_QUANTITY)
            .price(DEFAULT_PRICE)
            .createdTime(DEFAULT_CREATED_TIME)
            .effictiveTime(DEFAULT_EFFICTIVE_TIME)
            .expriedTime(DEFAULT_EXPRIED_TIME)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS);
        return spDeploy;
    }

    @Before
    public void initTest() {
        spDeploySearchRepository.deleteAll();
        spDeploy = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpDeploy() throws Exception {
        int databaseSizeBeforeCreate = spDeployRepository.findAll().size();

        // Create the SpDeploy
        SpDeployDTO spDeployDTO = spDeployMapper.toDto(spDeploy);
        restSpDeployMockMvc.perform(post("/api/sp-deploys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spDeployDTO)))
            .andExpect(status().isCreated());

        // Validate the SpDeploy in the database
        List<SpDeploy> spDeployList = spDeployRepository.findAll();
        assertThat(spDeployList).hasSize(databaseSizeBeforeCreate + 1);
        SpDeploy testSpDeploy = spDeployList.get(spDeployList.size() - 1);
        assertThat(testSpDeploy.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSpDeploy.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testSpDeploy.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testSpDeploy.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testSpDeploy.getEffictiveTime()).isEqualTo(DEFAULT_EFFICTIVE_TIME);
        assertThat(testSpDeploy.getExpriedTime()).isEqualTo(DEFAULT_EXPRIED_TIME);
        assertThat(testSpDeploy.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSpDeploy.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the SpDeploy in Elasticsearch
        SpDeploy spDeployEs = spDeploySearchRepository.findOne(testSpDeploy.getId());
        assertThat(spDeployEs).isEqualToComparingFieldByField(testSpDeploy);
    }

    @Test
    @Transactional
    public void createSpDeployWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = spDeployRepository.findAll().size();

        // Create the SpDeploy with an existing ID
        spDeploy.setId(1L);
        SpDeployDTO spDeployDTO = spDeployMapper.toDto(spDeploy);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpDeployMockMvc.perform(post("/api/sp-deploys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spDeployDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SpDeploy> spDeployList = spDeployRepository.findAll();
        assertThat(spDeployList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = spDeployRepository.findAll().size();
        // set the field null
        spDeploy.setName(null);

        // Create the SpDeploy, which fails.
        SpDeployDTO spDeployDTO = spDeployMapper.toDto(spDeploy);

        restSpDeployMockMvc.perform(post("/api/sp-deploys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spDeployDTO)))
            .andExpect(status().isBadRequest());

        List<SpDeploy> spDeployList = spDeployRepository.findAll();
        assertThat(spDeployList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = spDeployRepository.findAll().size();
        // set the field null
        spDeploy.setQuantity(null);

        // Create the SpDeploy, which fails.
        SpDeployDTO spDeployDTO = spDeployMapper.toDto(spDeploy);

        restSpDeployMockMvc.perform(post("/api/sp-deploys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spDeployDTO)))
            .andExpect(status().isBadRequest());

        List<SpDeploy> spDeployList = spDeployRepository.findAll();
        assertThat(spDeployList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = spDeployRepository.findAll().size();
        // set the field null
        spDeploy.setPrice(null);

        // Create the SpDeploy, which fails.
        SpDeployDTO spDeployDTO = spDeployMapper.toDto(spDeploy);

        restSpDeployMockMvc.perform(post("/api/sp-deploys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spDeployDTO)))
            .andExpect(status().isBadRequest());

        List<SpDeploy> spDeployList = spDeployRepository.findAll();
        assertThat(spDeployList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSpDeploys() throws Exception {
        // Initialize the database
        spDeployRepository.saveAndFlush(spDeploy);

        // Get all the spDeployList
        restSpDeployMockMvc.perform(get("/api/sp-deploys?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spDeploy.getId().intValue())))
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
    public void getSpDeploy() throws Exception {
        // Initialize the database
        spDeployRepository.saveAndFlush(spDeploy);

        // Get the spDeploy
        restSpDeployMockMvc.perform(get("/api/sp-deploys/{id}", spDeploy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(spDeploy.getId().intValue()))
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
    public void getNonExistingSpDeploy() throws Exception {
        // Get the spDeploy
        restSpDeployMockMvc.perform(get("/api/sp-deploys/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpDeploy() throws Exception {
        // Initialize the database
        spDeployRepository.saveAndFlush(spDeploy);
        spDeploySearchRepository.save(spDeploy);
        int databaseSizeBeforeUpdate = spDeployRepository.findAll().size();

        // Update the spDeploy
        SpDeploy updatedSpDeploy = spDeployRepository.findOne(spDeploy.getId());
        updatedSpDeploy
            .name(UPDATED_NAME)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .createdTime(UPDATED_CREATED_TIME)
            .effictiveTime(UPDATED_EFFICTIVE_TIME)
            .expriedTime(UPDATED_EXPRIED_TIME)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS);
        SpDeployDTO spDeployDTO = spDeployMapper.toDto(updatedSpDeploy);

        restSpDeployMockMvc.perform(put("/api/sp-deploys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spDeployDTO)))
            .andExpect(status().isOk());

        // Validate the SpDeploy in the database
        List<SpDeploy> spDeployList = spDeployRepository.findAll();
        assertThat(spDeployList).hasSize(databaseSizeBeforeUpdate);
        SpDeploy testSpDeploy = spDeployList.get(spDeployList.size() - 1);
        assertThat(testSpDeploy.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpDeploy.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testSpDeploy.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testSpDeploy.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testSpDeploy.getEffictiveTime()).isEqualTo(UPDATED_EFFICTIVE_TIME);
        assertThat(testSpDeploy.getExpriedTime()).isEqualTo(UPDATED_EXPRIED_TIME);
        assertThat(testSpDeploy.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSpDeploy.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the SpDeploy in Elasticsearch
        SpDeploy spDeployEs = spDeploySearchRepository.findOne(testSpDeploy.getId());
        assertThat(spDeployEs).isEqualToComparingFieldByField(testSpDeploy);
    }

    @Test
    @Transactional
    public void updateNonExistingSpDeploy() throws Exception {
        int databaseSizeBeforeUpdate = spDeployRepository.findAll().size();

        // Create the SpDeploy
        SpDeployDTO spDeployDTO = spDeployMapper.toDto(spDeploy);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSpDeployMockMvc.perform(put("/api/sp-deploys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spDeployDTO)))
            .andExpect(status().isCreated());

        // Validate the SpDeploy in the database
        List<SpDeploy> spDeployList = spDeployRepository.findAll();
        assertThat(spDeployList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSpDeploy() throws Exception {
        // Initialize the database
        spDeployRepository.saveAndFlush(spDeploy);
        spDeploySearchRepository.save(spDeploy);
        int databaseSizeBeforeDelete = spDeployRepository.findAll().size();

        // Get the spDeploy
        restSpDeployMockMvc.perform(delete("/api/sp-deploys/{id}", spDeploy.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean spDeployExistsInEs = spDeploySearchRepository.exists(spDeploy.getId());
        assertThat(spDeployExistsInEs).isFalse();

        // Validate the database is empty
        List<SpDeploy> spDeployList = spDeployRepository.findAll();
        assertThat(spDeployList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSpDeploy() throws Exception {
        // Initialize the database
        spDeployRepository.saveAndFlush(spDeploy);
        spDeploySearchRepository.save(spDeploy);

        // Search the spDeploy
        restSpDeployMockMvc.perform(get("/api/_search/sp-deploys?query=id:" + spDeploy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spDeploy.getId().intValue())))
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
        TestUtil.equalsVerifier(SpDeploy.class);
        SpDeploy spDeploy1 = new SpDeploy();
        spDeploy1.setId(1L);
        SpDeploy spDeploy2 = new SpDeploy();
        spDeploy2.setId(spDeploy1.getId());
        assertThat(spDeploy1).isEqualTo(spDeploy2);
        spDeploy2.setId(2L);
        assertThat(spDeploy1).isNotEqualTo(spDeploy2);
        spDeploy1.setId(null);
        assertThat(spDeploy1).isNotEqualTo(spDeploy2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpDeployDTO.class);
        SpDeployDTO spDeployDTO1 = new SpDeployDTO();
        spDeployDTO1.setId(1L);
        SpDeployDTO spDeployDTO2 = new SpDeployDTO();
        assertThat(spDeployDTO1).isNotEqualTo(spDeployDTO2);
        spDeployDTO2.setId(spDeployDTO1.getId());
        assertThat(spDeployDTO1).isEqualTo(spDeployDTO2);
        spDeployDTO2.setId(2L);
        assertThat(spDeployDTO1).isNotEqualTo(spDeployDTO2);
        spDeployDTO1.setId(null);
        assertThat(spDeployDTO1).isNotEqualTo(spDeployDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(spDeployMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(spDeployMapper.fromId(null)).isNull();
    }
}
