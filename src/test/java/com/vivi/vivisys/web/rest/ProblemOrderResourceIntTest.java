package com.vivi.vivisys.web.rest;

import com.vivi.vivisys.VivisysApp;

import com.vivi.vivisys.domain.ProblemOrder;
import com.vivi.vivisys.repository.ProblemOrderRepository;
import com.vivi.vivisys.service.ProblemOrderService;
import com.vivi.vivisys.repository.search.ProblemOrderSearchRepository;
import com.vivi.vivisys.service.dto.ProblemOrderDTO;
import com.vivi.vivisys.service.mapper.ProblemOrderMapper;
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
 * Test class for the ProblemOrderResource REST controller.
 *
 * @see ProblemOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VivisysApp.class)
public class ProblemOrderResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ProblemOrderRepository problemOrderRepository;

    @Autowired
    private ProblemOrderMapper problemOrderMapper;

    @Autowired
    private ProblemOrderService problemOrderService;

    @Autowired
    private ProblemOrderSearchRepository problemOrderSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProblemOrderMockMvc;

    private ProblemOrder problemOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProblemOrderResource problemOrderResource = new ProblemOrderResource(problemOrderService);
        this.restProblemOrderMockMvc = MockMvcBuilders.standaloneSetup(problemOrderResource)
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
    public static ProblemOrder createEntity(EntityManager em) {
        ProblemOrder problemOrder = new ProblemOrder()
            .name(DEFAULT_NAME);
        return problemOrder;
    }

    @Before
    public void initTest() {
        problemOrderSearchRepository.deleteAll();
        problemOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createProblemOrder() throws Exception {
        int databaseSizeBeforeCreate = problemOrderRepository.findAll().size();

        // Create the ProblemOrder
        ProblemOrderDTO problemOrderDTO = problemOrderMapper.toDto(problemOrder);
        restProblemOrderMockMvc.perform(post("/api/problem-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(problemOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the ProblemOrder in the database
        List<ProblemOrder> problemOrderList = problemOrderRepository.findAll();
        assertThat(problemOrderList).hasSize(databaseSizeBeforeCreate + 1);
        ProblemOrder testProblemOrder = problemOrderList.get(problemOrderList.size() - 1);
        assertThat(testProblemOrder.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the ProblemOrder in Elasticsearch
        ProblemOrder problemOrderEs = problemOrderSearchRepository.findOne(testProblemOrder.getId());
        assertThat(problemOrderEs).isEqualToComparingFieldByField(testProblemOrder);
    }

    @Test
    @Transactional
    public void createProblemOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = problemOrderRepository.findAll().size();

        // Create the ProblemOrder with an existing ID
        problemOrder.setId(1L);
        ProblemOrderDTO problemOrderDTO = problemOrderMapper.toDto(problemOrder);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProblemOrderMockMvc.perform(post("/api/problem-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(problemOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ProblemOrder> problemOrderList = problemOrderRepository.findAll();
        assertThat(problemOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProblemOrders() throws Exception {
        // Initialize the database
        problemOrderRepository.saveAndFlush(problemOrder);

        // Get all the problemOrderList
        restProblemOrderMockMvc.perform(get("/api/problem-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(problemOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getProblemOrder() throws Exception {
        // Initialize the database
        problemOrderRepository.saveAndFlush(problemOrder);

        // Get the problemOrder
        restProblemOrderMockMvc.perform(get("/api/problem-orders/{id}", problemOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(problemOrder.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProblemOrder() throws Exception {
        // Get the problemOrder
        restProblemOrderMockMvc.perform(get("/api/problem-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProblemOrder() throws Exception {
        // Initialize the database
        problemOrderRepository.saveAndFlush(problemOrder);
        problemOrderSearchRepository.save(problemOrder);
        int databaseSizeBeforeUpdate = problemOrderRepository.findAll().size();

        // Update the problemOrder
        ProblemOrder updatedProblemOrder = problemOrderRepository.findOne(problemOrder.getId());
        updatedProblemOrder
            .name(UPDATED_NAME);
        ProblemOrderDTO problemOrderDTO = problemOrderMapper.toDto(updatedProblemOrder);

        restProblemOrderMockMvc.perform(put("/api/problem-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(problemOrderDTO)))
            .andExpect(status().isOk());

        // Validate the ProblemOrder in the database
        List<ProblemOrder> problemOrderList = problemOrderRepository.findAll();
        assertThat(problemOrderList).hasSize(databaseSizeBeforeUpdate);
        ProblemOrder testProblemOrder = problemOrderList.get(problemOrderList.size() - 1);
        assertThat(testProblemOrder.getName()).isEqualTo(UPDATED_NAME);

        // Validate the ProblemOrder in Elasticsearch
        ProblemOrder problemOrderEs = problemOrderSearchRepository.findOne(testProblemOrder.getId());
        assertThat(problemOrderEs).isEqualToComparingFieldByField(testProblemOrder);
    }

    @Test
    @Transactional
    public void updateNonExistingProblemOrder() throws Exception {
        int databaseSizeBeforeUpdate = problemOrderRepository.findAll().size();

        // Create the ProblemOrder
        ProblemOrderDTO problemOrderDTO = problemOrderMapper.toDto(problemOrder);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProblemOrderMockMvc.perform(put("/api/problem-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(problemOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the ProblemOrder in the database
        List<ProblemOrder> problemOrderList = problemOrderRepository.findAll();
        assertThat(problemOrderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProblemOrder() throws Exception {
        // Initialize the database
        problemOrderRepository.saveAndFlush(problemOrder);
        problemOrderSearchRepository.save(problemOrder);
        int databaseSizeBeforeDelete = problemOrderRepository.findAll().size();

        // Get the problemOrder
        restProblemOrderMockMvc.perform(delete("/api/problem-orders/{id}", problemOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean problemOrderExistsInEs = problemOrderSearchRepository.exists(problemOrder.getId());
        assertThat(problemOrderExistsInEs).isFalse();

        // Validate the database is empty
        List<ProblemOrder> problemOrderList = problemOrderRepository.findAll();
        assertThat(problemOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProblemOrder() throws Exception {
        // Initialize the database
        problemOrderRepository.saveAndFlush(problemOrder);
        problemOrderSearchRepository.save(problemOrder);

        // Search the problemOrder
        restProblemOrderMockMvc.perform(get("/api/_search/problem-orders?query=id:" + problemOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(problemOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProblemOrder.class);
        ProblemOrder problemOrder1 = new ProblemOrder();
        problemOrder1.setId(1L);
        ProblemOrder problemOrder2 = new ProblemOrder();
        problemOrder2.setId(problemOrder1.getId());
        assertThat(problemOrder1).isEqualTo(problemOrder2);
        problemOrder2.setId(2L);
        assertThat(problemOrder1).isNotEqualTo(problemOrder2);
        problemOrder1.setId(null);
        assertThat(problemOrder1).isNotEqualTo(problemOrder2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProblemOrderDTO.class);
        ProblemOrderDTO problemOrderDTO1 = new ProblemOrderDTO();
        problemOrderDTO1.setId(1L);
        ProblemOrderDTO problemOrderDTO2 = new ProblemOrderDTO();
        assertThat(problemOrderDTO1).isNotEqualTo(problemOrderDTO2);
        problemOrderDTO2.setId(problemOrderDTO1.getId());
        assertThat(problemOrderDTO1).isEqualTo(problemOrderDTO2);
        problemOrderDTO2.setId(2L);
        assertThat(problemOrderDTO1).isNotEqualTo(problemOrderDTO2);
        problemOrderDTO1.setId(null);
        assertThat(problemOrderDTO1).isNotEqualTo(problemOrderDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(problemOrderMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(problemOrderMapper.fromId(null)).isNull();
    }
}
