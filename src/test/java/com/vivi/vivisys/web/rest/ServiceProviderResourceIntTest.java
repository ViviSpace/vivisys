package com.vivi.vivisys.web.rest;

import com.vivi.vivisys.VivisysApp;

import com.vivi.vivisys.domain.ServiceProvider;
import com.vivi.vivisys.repository.ServiceProviderRepository;
import com.vivi.vivisys.service.ServiceProviderService;
import com.vivi.vivisys.repository.search.ServiceProviderSearchRepository;
import com.vivi.vivisys.service.dto.ServiceProviderDTO;
import com.vivi.vivisys.service.mapper.ServiceProviderMapper;
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
 * Test class for the ServiceProviderResource REST controller.
 *
 * @see ServiceProviderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VivisysApp.class)
public class ServiceProviderResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private ServiceProviderMapper serviceProviderMapper;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private ServiceProviderSearchRepository serviceProviderSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restServiceProviderMockMvc;

    private ServiceProvider serviceProvider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ServiceProviderResource serviceProviderResource = new ServiceProviderResource(serviceProviderService);
        this.restServiceProviderMockMvc = MockMvcBuilders.standaloneSetup(serviceProviderResource)
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
    public static ServiceProvider createEntity(EntityManager em) {
        ServiceProvider serviceProvider = new ServiceProvider()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS);
        return serviceProvider;
    }

    @Before
    public void initTest() {
        serviceProviderSearchRepository.deleteAll();
        serviceProvider = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceProvider() throws Exception {
        int databaseSizeBeforeCreate = serviceProviderRepository.findAll().size();

        // Create the ServiceProvider
        ServiceProviderDTO serviceProviderDTO = serviceProviderMapper.toDto(serviceProvider);
        restServiceProviderMockMvc.perform(post("/api/service-providers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceProviderDTO)))
            .andExpect(status().isCreated());

        // Validate the ServiceProvider in the database
        List<ServiceProvider> serviceProviderList = serviceProviderRepository.findAll();
        assertThat(serviceProviderList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceProvider testServiceProvider = serviceProviderList.get(serviceProviderList.size() - 1);
        assertThat(testServiceProvider.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testServiceProvider.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testServiceProvider.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testServiceProvider.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the ServiceProvider in Elasticsearch
        ServiceProvider serviceProviderEs = serviceProviderSearchRepository.findOne(testServiceProvider.getId());
        assertThat(serviceProviderEs).isEqualToComparingFieldByField(testServiceProvider);
    }

    @Test
    @Transactional
    public void createServiceProviderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceProviderRepository.findAll().size();

        // Create the ServiceProvider with an existing ID
        serviceProvider.setId(1L);
        ServiceProviderDTO serviceProviderDTO = serviceProviderMapper.toDto(serviceProvider);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceProviderMockMvc.perform(post("/api/service-providers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceProviderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ServiceProvider> serviceProviderList = serviceProviderRepository.findAll();
        assertThat(serviceProviderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceProviderRepository.findAll().size();
        // set the field null
        serviceProvider.setName(null);

        // Create the ServiceProvider, which fails.
        ServiceProviderDTO serviceProviderDTO = serviceProviderMapper.toDto(serviceProvider);

        restServiceProviderMockMvc.perform(post("/api/service-providers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceProviderDTO)))
            .andExpect(status().isBadRequest());

        List<ServiceProvider> serviceProviderList = serviceProviderRepository.findAll();
        assertThat(serviceProviderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServiceProviders() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList
        restServiceProviderMockMvc.perform(get("/api/service-providers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceProvider.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getServiceProvider() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get the serviceProvider
        restServiceProviderMockMvc.perform(get("/api/service-providers/{id}", serviceProvider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviceProvider.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceProvider() throws Exception {
        // Get the serviceProvider
        restServiceProviderMockMvc.perform(get("/api/service-providers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceProvider() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);
        serviceProviderSearchRepository.save(serviceProvider);
        int databaseSizeBeforeUpdate = serviceProviderRepository.findAll().size();

        // Update the serviceProvider
        ServiceProvider updatedServiceProvider = serviceProviderRepository.findOne(serviceProvider.getId());
        updatedServiceProvider
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS);
        ServiceProviderDTO serviceProviderDTO = serviceProviderMapper.toDto(updatedServiceProvider);

        restServiceProviderMockMvc.perform(put("/api/service-providers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceProviderDTO)))
            .andExpect(status().isOk());

        // Validate the ServiceProvider in the database
        List<ServiceProvider> serviceProviderList = serviceProviderRepository.findAll();
        assertThat(serviceProviderList).hasSize(databaseSizeBeforeUpdate);
        ServiceProvider testServiceProvider = serviceProviderList.get(serviceProviderList.size() - 1);
        assertThat(testServiceProvider.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testServiceProvider.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testServiceProvider.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testServiceProvider.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the ServiceProvider in Elasticsearch
        ServiceProvider serviceProviderEs = serviceProviderSearchRepository.findOne(testServiceProvider.getId());
        assertThat(serviceProviderEs).isEqualToComparingFieldByField(testServiceProvider);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceProvider() throws Exception {
        int databaseSizeBeforeUpdate = serviceProviderRepository.findAll().size();

        // Create the ServiceProvider
        ServiceProviderDTO serviceProviderDTO = serviceProviderMapper.toDto(serviceProvider);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restServiceProviderMockMvc.perform(put("/api/service-providers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceProviderDTO)))
            .andExpect(status().isCreated());

        // Validate the ServiceProvider in the database
        List<ServiceProvider> serviceProviderList = serviceProviderRepository.findAll();
        assertThat(serviceProviderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteServiceProvider() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);
        serviceProviderSearchRepository.save(serviceProvider);
        int databaseSizeBeforeDelete = serviceProviderRepository.findAll().size();

        // Get the serviceProvider
        restServiceProviderMockMvc.perform(delete("/api/service-providers/{id}", serviceProvider.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean serviceProviderExistsInEs = serviceProviderSearchRepository.exists(serviceProvider.getId());
        assertThat(serviceProviderExistsInEs).isFalse();

        // Validate the database is empty
        List<ServiceProvider> serviceProviderList = serviceProviderRepository.findAll();
        assertThat(serviceProviderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchServiceProvider() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);
        serviceProviderSearchRepository.save(serviceProvider);

        // Search the serviceProvider
        restServiceProviderMockMvc.perform(get("/api/_search/service-providers?query=id:" + serviceProvider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceProvider.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceProvider.class);
        ServiceProvider serviceProvider1 = new ServiceProvider();
        serviceProvider1.setId(1L);
        ServiceProvider serviceProvider2 = new ServiceProvider();
        serviceProvider2.setId(serviceProvider1.getId());
        assertThat(serviceProvider1).isEqualTo(serviceProvider2);
        serviceProvider2.setId(2L);
        assertThat(serviceProvider1).isNotEqualTo(serviceProvider2);
        serviceProvider1.setId(null);
        assertThat(serviceProvider1).isNotEqualTo(serviceProvider2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceProviderDTO.class);
        ServiceProviderDTO serviceProviderDTO1 = new ServiceProviderDTO();
        serviceProviderDTO1.setId(1L);
        ServiceProviderDTO serviceProviderDTO2 = new ServiceProviderDTO();
        assertThat(serviceProviderDTO1).isNotEqualTo(serviceProviderDTO2);
        serviceProviderDTO2.setId(serviceProviderDTO1.getId());
        assertThat(serviceProviderDTO1).isEqualTo(serviceProviderDTO2);
        serviceProviderDTO2.setId(2L);
        assertThat(serviceProviderDTO1).isNotEqualTo(serviceProviderDTO2);
        serviceProviderDTO1.setId(null);
        assertThat(serviceProviderDTO1).isNotEqualTo(serviceProviderDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(serviceProviderMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(serviceProviderMapper.fromId(null)).isNull();
    }
}
