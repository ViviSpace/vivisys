package com.vivi.vivisys.web.rest;

import com.vivi.vivisys.VivisysApp;

import com.vivi.vivisys.domain.CustomerIncome;
import com.vivi.vivisys.repository.CustomerIncomeRepository;
import com.vivi.vivisys.service.CustomerIncomeService;
import com.vivi.vivisys.repository.search.CustomerIncomeSearchRepository;
import com.vivi.vivisys.service.dto.CustomerIncomeDTO;
import com.vivi.vivisys.service.mapper.CustomerIncomeMapper;
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
 * Test class for the CustomerIncomeResource REST controller.
 *
 * @see CustomerIncomeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VivisysApp.class)
public class CustomerIncomeResourceIntTest {

    @Autowired
    private CustomerIncomeRepository customerIncomeRepository;

    @Autowired
    private CustomerIncomeMapper customerIncomeMapper;

    @Autowired
    private CustomerIncomeService customerIncomeService;

    @Autowired
    private CustomerIncomeSearchRepository customerIncomeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCustomerIncomeMockMvc;

    private CustomerIncome customerIncome;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerIncomeResource customerIncomeResource = new CustomerIncomeResource(customerIncomeService);
        this.restCustomerIncomeMockMvc = MockMvcBuilders.standaloneSetup(customerIncomeResource)
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
    public static CustomerIncome createEntity(EntityManager em) {
        CustomerIncome customerIncome = new CustomerIncome();
        return customerIncome;
    }

    @Before
    public void initTest() {
        customerIncomeSearchRepository.deleteAll();
        customerIncome = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomerIncome() throws Exception {
        int databaseSizeBeforeCreate = customerIncomeRepository.findAll().size();

        // Create the CustomerIncome
        CustomerIncomeDTO customerIncomeDTO = customerIncomeMapper.toDto(customerIncome);
        restCustomerIncomeMockMvc.perform(post("/api/customer-incomes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerIncomeDTO)))
            .andExpect(status().isCreated());

        // Validate the CustomerIncome in the database
        List<CustomerIncome> customerIncomeList = customerIncomeRepository.findAll();
        assertThat(customerIncomeList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerIncome testCustomerIncome = customerIncomeList.get(customerIncomeList.size() - 1);

        // Validate the CustomerIncome in Elasticsearch
        CustomerIncome customerIncomeEs = customerIncomeSearchRepository.findOne(testCustomerIncome.getId());
        assertThat(customerIncomeEs).isEqualToComparingFieldByField(testCustomerIncome);
    }

    @Test
    @Transactional
    public void createCustomerIncomeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerIncomeRepository.findAll().size();

        // Create the CustomerIncome with an existing ID
        customerIncome.setId(1L);
        CustomerIncomeDTO customerIncomeDTO = customerIncomeMapper.toDto(customerIncome);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerIncomeMockMvc.perform(post("/api/customer-incomes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerIncomeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CustomerIncome> customerIncomeList = customerIncomeRepository.findAll();
        assertThat(customerIncomeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCustomerIncomes() throws Exception {
        // Initialize the database
        customerIncomeRepository.saveAndFlush(customerIncome);

        // Get all the customerIncomeList
        restCustomerIncomeMockMvc.perform(get("/api/customer-incomes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerIncome.getId().intValue())));
    }

    @Test
    @Transactional
    public void getCustomerIncome() throws Exception {
        // Initialize the database
        customerIncomeRepository.saveAndFlush(customerIncome);

        // Get the customerIncome
        restCustomerIncomeMockMvc.perform(get("/api/customer-incomes/{id}", customerIncome.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customerIncome.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerIncome() throws Exception {
        // Get the customerIncome
        restCustomerIncomeMockMvc.perform(get("/api/customer-incomes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerIncome() throws Exception {
        // Initialize the database
        customerIncomeRepository.saveAndFlush(customerIncome);
        customerIncomeSearchRepository.save(customerIncome);
        int databaseSizeBeforeUpdate = customerIncomeRepository.findAll().size();

        // Update the customerIncome
        CustomerIncome updatedCustomerIncome = customerIncomeRepository.findOne(customerIncome.getId());
        CustomerIncomeDTO customerIncomeDTO = customerIncomeMapper.toDto(updatedCustomerIncome);

        restCustomerIncomeMockMvc.perform(put("/api/customer-incomes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerIncomeDTO)))
            .andExpect(status().isOk());

        // Validate the CustomerIncome in the database
        List<CustomerIncome> customerIncomeList = customerIncomeRepository.findAll();
        assertThat(customerIncomeList).hasSize(databaseSizeBeforeUpdate);
        CustomerIncome testCustomerIncome = customerIncomeList.get(customerIncomeList.size() - 1);

        // Validate the CustomerIncome in Elasticsearch
        CustomerIncome customerIncomeEs = customerIncomeSearchRepository.findOne(testCustomerIncome.getId());
        assertThat(customerIncomeEs).isEqualToComparingFieldByField(testCustomerIncome);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomerIncome() throws Exception {
        int databaseSizeBeforeUpdate = customerIncomeRepository.findAll().size();

        // Create the CustomerIncome
        CustomerIncomeDTO customerIncomeDTO = customerIncomeMapper.toDto(customerIncome);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCustomerIncomeMockMvc.perform(put("/api/customer-incomes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerIncomeDTO)))
            .andExpect(status().isCreated());

        // Validate the CustomerIncome in the database
        List<CustomerIncome> customerIncomeList = customerIncomeRepository.findAll();
        assertThat(customerIncomeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCustomerIncome() throws Exception {
        // Initialize the database
        customerIncomeRepository.saveAndFlush(customerIncome);
        customerIncomeSearchRepository.save(customerIncome);
        int databaseSizeBeforeDelete = customerIncomeRepository.findAll().size();

        // Get the customerIncome
        restCustomerIncomeMockMvc.perform(delete("/api/customer-incomes/{id}", customerIncome.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean customerIncomeExistsInEs = customerIncomeSearchRepository.exists(customerIncome.getId());
        assertThat(customerIncomeExistsInEs).isFalse();

        // Validate the database is empty
        List<CustomerIncome> customerIncomeList = customerIncomeRepository.findAll();
        assertThat(customerIncomeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCustomerIncome() throws Exception {
        // Initialize the database
        customerIncomeRepository.saveAndFlush(customerIncome);
        customerIncomeSearchRepository.save(customerIncome);

        // Search the customerIncome
        restCustomerIncomeMockMvc.perform(get("/api/_search/customer-incomes?query=id:" + customerIncome.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerIncome.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerIncome.class);
        CustomerIncome customerIncome1 = new CustomerIncome();
        customerIncome1.setId(1L);
        CustomerIncome customerIncome2 = new CustomerIncome();
        customerIncome2.setId(customerIncome1.getId());
        assertThat(customerIncome1).isEqualTo(customerIncome2);
        customerIncome2.setId(2L);
        assertThat(customerIncome1).isNotEqualTo(customerIncome2);
        customerIncome1.setId(null);
        assertThat(customerIncome1).isNotEqualTo(customerIncome2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerIncomeDTO.class);
        CustomerIncomeDTO customerIncomeDTO1 = new CustomerIncomeDTO();
        customerIncomeDTO1.setId(1L);
        CustomerIncomeDTO customerIncomeDTO2 = new CustomerIncomeDTO();
        assertThat(customerIncomeDTO1).isNotEqualTo(customerIncomeDTO2);
        customerIncomeDTO2.setId(customerIncomeDTO1.getId());
        assertThat(customerIncomeDTO1).isEqualTo(customerIncomeDTO2);
        customerIncomeDTO2.setId(2L);
        assertThat(customerIncomeDTO1).isNotEqualTo(customerIncomeDTO2);
        customerIncomeDTO1.setId(null);
        assertThat(customerIncomeDTO1).isNotEqualTo(customerIncomeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(customerIncomeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(customerIncomeMapper.fromId(null)).isNull();
    }
}
