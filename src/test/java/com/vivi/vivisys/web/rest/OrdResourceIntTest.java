package com.vivi.vivisys.web.rest;

import com.vivi.vivisys.VivisysApp;

import com.vivi.vivisys.domain.Ord;
import com.vivi.vivisys.repository.OrdRepository;
import com.vivi.vivisys.service.OrdService;
import com.vivi.vivisys.repository.search.OrdSearchRepository;
import com.vivi.vivisys.service.dto.OrdDTO;
import com.vivi.vivisys.service.mapper.OrdMapper;
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
 * Test class for the OrdResource REST controller.
 *
 * @see OrdResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VivisysApp.class)
public class OrdResourceIntTest {

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
    private OrdRepository ordRepository;

    @Autowired
    private OrdMapper ordMapper;

    @Autowired
    private OrdService ordService;

    @Autowired
    private OrdSearchRepository ordSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrdMockMvc;

    private Ord ord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrdResource ordResource = new OrdResource(ordService);
        this.restOrdMockMvc = MockMvcBuilders.standaloneSetup(ordResource)
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
    public static Ord createEntity(EntityManager em) {
        Ord ord = new Ord()
            .name(DEFAULT_NAME)
            .quantity(DEFAULT_QUANTITY)
            .price(DEFAULT_PRICE)
            .createdTime(DEFAULT_CREATED_TIME)
            .effictiveTime(DEFAULT_EFFICTIVE_TIME)
            .expriedTime(DEFAULT_EXPRIED_TIME)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS);
        return ord;
    }

    @Before
    public void initTest() {
        ordSearchRepository.deleteAll();
        ord = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrd() throws Exception {
        int databaseSizeBeforeCreate = ordRepository.findAll().size();

        // Create the Ord
        OrdDTO ordDTO = ordMapper.toDto(ord);
        restOrdMockMvc.perform(post("/api/ords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordDTO)))
            .andExpect(status().isCreated());

        // Validate the Ord in the database
        List<Ord> ordList = ordRepository.findAll();
        assertThat(ordList).hasSize(databaseSizeBeforeCreate + 1);
        Ord testOrd = ordList.get(ordList.size() - 1);
        assertThat(testOrd.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrd.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrd.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOrd.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testOrd.getEffictiveTime()).isEqualTo(DEFAULT_EFFICTIVE_TIME);
        assertThat(testOrd.getExpriedTime()).isEqualTo(DEFAULT_EXPRIED_TIME);
        assertThat(testOrd.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testOrd.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Ord in Elasticsearch
        Ord ordEs = ordSearchRepository.findOne(testOrd.getId());
        assertThat(ordEs).isEqualToComparingFieldByField(testOrd);
    }

    @Test
    @Transactional
    public void createOrdWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ordRepository.findAll().size();

        // Create the Ord with an existing ID
        ord.setId(1L);
        OrdDTO ordDTO = ordMapper.toDto(ord);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdMockMvc.perform(post("/api/ords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Ord> ordList = ordRepository.findAll();
        assertThat(ordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordRepository.findAll().size();
        // set the field null
        ord.setName(null);

        // Create the Ord, which fails.
        OrdDTO ordDTO = ordMapper.toDto(ord);

        restOrdMockMvc.perform(post("/api/ords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordDTO)))
            .andExpect(status().isBadRequest());

        List<Ord> ordList = ordRepository.findAll();
        assertThat(ordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordRepository.findAll().size();
        // set the field null
        ord.setQuantity(null);

        // Create the Ord, which fails.
        OrdDTO ordDTO = ordMapper.toDto(ord);

        restOrdMockMvc.perform(post("/api/ords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordDTO)))
            .andExpect(status().isBadRequest());

        List<Ord> ordList = ordRepository.findAll();
        assertThat(ordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordRepository.findAll().size();
        // set the field null
        ord.setPrice(null);

        // Create the Ord, which fails.
        OrdDTO ordDTO = ordMapper.toDto(ord);

        restOrdMockMvc.perform(post("/api/ords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordDTO)))
            .andExpect(status().isBadRequest());

        List<Ord> ordList = ordRepository.findAll();
        assertThat(ordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrds() throws Exception {
        // Initialize the database
        ordRepository.saveAndFlush(ord);

        // Get all the ordList
        restOrdMockMvc.perform(get("/api/ords?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ord.getId().intValue())))
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
    public void getOrd() throws Exception {
        // Initialize the database
        ordRepository.saveAndFlush(ord);

        // Get the ord
        restOrdMockMvc.perform(get("/api/ords/{id}", ord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ord.getId().intValue()))
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
    public void getNonExistingOrd() throws Exception {
        // Get the ord
        restOrdMockMvc.perform(get("/api/ords/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrd() throws Exception {
        // Initialize the database
        ordRepository.saveAndFlush(ord);
        ordSearchRepository.save(ord);
        int databaseSizeBeforeUpdate = ordRepository.findAll().size();

        // Update the ord
        Ord updatedOrd = ordRepository.findOne(ord.getId());
        updatedOrd
            .name(UPDATED_NAME)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .createdTime(UPDATED_CREATED_TIME)
            .effictiveTime(UPDATED_EFFICTIVE_TIME)
            .expriedTime(UPDATED_EXPRIED_TIME)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS);
        OrdDTO ordDTO = ordMapper.toDto(updatedOrd);

        restOrdMockMvc.perform(put("/api/ords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordDTO)))
            .andExpect(status().isOk());

        // Validate the Ord in the database
        List<Ord> ordList = ordRepository.findAll();
        assertThat(ordList).hasSize(databaseSizeBeforeUpdate);
        Ord testOrd = ordList.get(ordList.size() - 1);
        assertThat(testOrd.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrd.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrd.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOrd.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testOrd.getEffictiveTime()).isEqualTo(UPDATED_EFFICTIVE_TIME);
        assertThat(testOrd.getExpriedTime()).isEqualTo(UPDATED_EXPRIED_TIME);
        assertThat(testOrd.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOrd.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Ord in Elasticsearch
        Ord ordEs = ordSearchRepository.findOne(testOrd.getId());
        assertThat(ordEs).isEqualToComparingFieldByField(testOrd);
    }

    @Test
    @Transactional
    public void updateNonExistingOrd() throws Exception {
        int databaseSizeBeforeUpdate = ordRepository.findAll().size();

        // Create the Ord
        OrdDTO ordDTO = ordMapper.toDto(ord);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOrdMockMvc.perform(put("/api/ords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordDTO)))
            .andExpect(status().isCreated());

        // Validate the Ord in the database
        List<Ord> ordList = ordRepository.findAll();
        assertThat(ordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOrd() throws Exception {
        // Initialize the database
        ordRepository.saveAndFlush(ord);
        ordSearchRepository.save(ord);
        int databaseSizeBeforeDelete = ordRepository.findAll().size();

        // Get the ord
        restOrdMockMvc.perform(delete("/api/ords/{id}", ord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean ordExistsInEs = ordSearchRepository.exists(ord.getId());
        assertThat(ordExistsInEs).isFalse();

        // Validate the database is empty
        List<Ord> ordList = ordRepository.findAll();
        assertThat(ordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOrd() throws Exception {
        // Initialize the database
        ordRepository.saveAndFlush(ord);
        ordSearchRepository.save(ord);

        // Search the ord
        restOrdMockMvc.perform(get("/api/_search/ords?query=id:" + ord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ord.getId().intValue())))
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
        TestUtil.equalsVerifier(Ord.class);
        Ord ord1 = new Ord();
        ord1.setId(1L);
        Ord ord2 = new Ord();
        ord2.setId(ord1.getId());
        assertThat(ord1).isEqualTo(ord2);
        ord2.setId(2L);
        assertThat(ord1).isNotEqualTo(ord2);
        ord1.setId(null);
        assertThat(ord1).isNotEqualTo(ord2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrdDTO.class);
        OrdDTO ordDTO1 = new OrdDTO();
        ordDTO1.setId(1L);
        OrdDTO ordDTO2 = new OrdDTO();
        assertThat(ordDTO1).isNotEqualTo(ordDTO2);
        ordDTO2.setId(ordDTO1.getId());
        assertThat(ordDTO1).isEqualTo(ordDTO2);
        ordDTO2.setId(2L);
        assertThat(ordDTO1).isNotEqualTo(ordDTO2);
        ordDTO1.setId(null);
        assertThat(ordDTO1).isNotEqualTo(ordDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(ordMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(ordMapper.fromId(null)).isNull();
    }
}
