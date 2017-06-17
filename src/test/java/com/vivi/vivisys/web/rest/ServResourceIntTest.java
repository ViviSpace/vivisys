package com.vivi.vivisys.web.rest;

import com.vivi.vivisys.VivisysApp;

import com.vivi.vivisys.domain.Serv;
import com.vivi.vivisys.repository.ServRepository;
import com.vivi.vivisys.service.ServService;
import com.vivi.vivisys.repository.search.ServSearchRepository;
import com.vivi.vivisys.service.dto.ServDTO;
import com.vivi.vivisys.service.mapper.ServMapper;
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
 * Test class for the ServResource REST controller.
 *
 * @see ServResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VivisysApp.class)
public class ServResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTIN = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTIN = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private ServRepository servRepository;

    @Autowired
    private ServMapper servMapper;

    @Autowired
    private ServService servService;

    @Autowired
    private ServSearchRepository servSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restServMockMvc;

    private Serv serv;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ServResource servResource = new ServResource(servService);
        this.restServMockMvc = MockMvcBuilders.standaloneSetup(servResource)
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
    public static Serv createEntity(EntityManager em) {
        Serv serv = new Serv()
            .name(DEFAULT_NAME)
            .descriptin(DEFAULT_DESCRIPTIN)
            .price(DEFAULT_PRICE)
            .unit(DEFAULT_UNIT)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS);
        return serv;
    }

    @Before
    public void initTest() {
        servSearchRepository.deleteAll();
        serv = createEntity(em);
    }

    @Test
    @Transactional
    public void createServ() throws Exception {
        int databaseSizeBeforeCreate = servRepository.findAll().size();

        // Create the Serv
        ServDTO servDTO = servMapper.toDto(serv);
        restServMockMvc.perform(post("/api/servs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servDTO)))
            .andExpect(status().isCreated());

        // Validate the Serv in the database
        List<Serv> servList = servRepository.findAll();
        assertThat(servList).hasSize(databaseSizeBeforeCreate + 1);
        Serv testServ = servList.get(servList.size() - 1);
        assertThat(testServ.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testServ.getDescriptin()).isEqualTo(DEFAULT_DESCRIPTIN);
        assertThat(testServ.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testServ.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testServ.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testServ.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Serv in Elasticsearch
        Serv servEs = servSearchRepository.findOne(testServ.getId());
        assertThat(servEs).isEqualToComparingFieldByField(testServ);
    }

    @Test
    @Transactional
    public void createServWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = servRepository.findAll().size();

        // Create the Serv with an existing ID
        serv.setId(1L);
        ServDTO servDTO = servMapper.toDto(serv);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServMockMvc.perform(post("/api/servs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Serv> servList = servRepository.findAll();
        assertThat(servList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = servRepository.findAll().size();
        // set the field null
        serv.setName(null);

        // Create the Serv, which fails.
        ServDTO servDTO = servMapper.toDto(serv);

        restServMockMvc.perform(post("/api/servs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servDTO)))
            .andExpect(status().isBadRequest());

        List<Serv> servList = servRepository.findAll();
        assertThat(servList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = servRepository.findAll().size();
        // set the field null
        serv.setPrice(null);

        // Create the Serv, which fails.
        ServDTO servDTO = servMapper.toDto(serv);

        restServMockMvc.perform(post("/api/servs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servDTO)))
            .andExpect(status().isBadRequest());

        List<Serv> servList = servRepository.findAll();
        assertThat(servList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = servRepository.findAll().size();
        // set the field null
        serv.setUnit(null);

        // Create the Serv, which fails.
        ServDTO servDTO = servMapper.toDto(serv);

        restServMockMvc.perform(post("/api/servs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servDTO)))
            .andExpect(status().isBadRequest());

        List<Serv> servList = servRepository.findAll();
        assertThat(servList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServs() throws Exception {
        // Initialize the database
        servRepository.saveAndFlush(serv);

        // Get all the servList
        restServMockMvc.perform(get("/api/servs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serv.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].descriptin").value(hasItem(DEFAULT_DESCRIPTIN.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getServ() throws Exception {
        // Initialize the database
        servRepository.saveAndFlush(serv);

        // Get the serv
        restServMockMvc.perform(get("/api/servs/{id}", serv.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serv.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.descriptin").value(DEFAULT_DESCRIPTIN.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServ() throws Exception {
        // Get the serv
        restServMockMvc.perform(get("/api/servs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServ() throws Exception {
        // Initialize the database
        servRepository.saveAndFlush(serv);
        servSearchRepository.save(serv);
        int databaseSizeBeforeUpdate = servRepository.findAll().size();

        // Update the serv
        Serv updatedServ = servRepository.findOne(serv.getId());
        updatedServ
            .name(UPDATED_NAME)
            .descriptin(UPDATED_DESCRIPTIN)
            .price(UPDATED_PRICE)
            .unit(UPDATED_UNIT)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS);
        ServDTO servDTO = servMapper.toDto(updatedServ);

        restServMockMvc.perform(put("/api/servs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servDTO)))
            .andExpect(status().isOk());

        // Validate the Serv in the database
        List<Serv> servList = servRepository.findAll();
        assertThat(servList).hasSize(databaseSizeBeforeUpdate);
        Serv testServ = servList.get(servList.size() - 1);
        assertThat(testServ.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testServ.getDescriptin()).isEqualTo(UPDATED_DESCRIPTIN);
        assertThat(testServ.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testServ.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testServ.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testServ.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Serv in Elasticsearch
        Serv servEs = servSearchRepository.findOne(testServ.getId());
        assertThat(servEs).isEqualToComparingFieldByField(testServ);
    }

    @Test
    @Transactional
    public void updateNonExistingServ() throws Exception {
        int databaseSizeBeforeUpdate = servRepository.findAll().size();

        // Create the Serv
        ServDTO servDTO = servMapper.toDto(serv);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restServMockMvc.perform(put("/api/servs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servDTO)))
            .andExpect(status().isCreated());

        // Validate the Serv in the database
        List<Serv> servList = servRepository.findAll();
        assertThat(servList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteServ() throws Exception {
        // Initialize the database
        servRepository.saveAndFlush(serv);
        servSearchRepository.save(serv);
        int databaseSizeBeforeDelete = servRepository.findAll().size();

        // Get the serv
        restServMockMvc.perform(delete("/api/servs/{id}", serv.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean servExistsInEs = servSearchRepository.exists(serv.getId());
        assertThat(servExistsInEs).isFalse();

        // Validate the database is empty
        List<Serv> servList = servRepository.findAll();
        assertThat(servList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchServ() throws Exception {
        // Initialize the database
        servRepository.saveAndFlush(serv);
        servSearchRepository.save(serv);

        // Search the serv
        restServMockMvc.perform(get("/api/_search/servs?query=id:" + serv.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serv.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].descriptin").value(hasItem(DEFAULT_DESCRIPTIN.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Serv.class);
        Serv serv1 = new Serv();
        serv1.setId(1L);
        Serv serv2 = new Serv();
        serv2.setId(serv1.getId());
        assertThat(serv1).isEqualTo(serv2);
        serv2.setId(2L);
        assertThat(serv1).isNotEqualTo(serv2);
        serv1.setId(null);
        assertThat(serv1).isNotEqualTo(serv2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServDTO.class);
        ServDTO servDTO1 = new ServDTO();
        servDTO1.setId(1L);
        ServDTO servDTO2 = new ServDTO();
        assertThat(servDTO1).isNotEqualTo(servDTO2);
        servDTO2.setId(servDTO1.getId());
        assertThat(servDTO1).isEqualTo(servDTO2);
        servDTO2.setId(2L);
        assertThat(servDTO1).isNotEqualTo(servDTO2);
        servDTO1.setId(null);
        assertThat(servDTO1).isNotEqualTo(servDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(servMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(servMapper.fromId(null)).isNull();
    }
}
