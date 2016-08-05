package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MyHrAppConcediuApp;
import com.mycompany.myapp.domain.VacationStock;
import com.mycompany.myapp.repository.VacationStockRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the VacationStockResource REST controller.
 *
 * @see VacationStockResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyHrAppConcediuApp.class)
@WebAppConfiguration
@IntegrationTest
public class VacationStockResourceIntTest {


    private static final BigDecimal DEFAULT_FREE_DAYS = new BigDecimal(1);
    private static final BigDecimal UPDATED_FREE_DAYS = new BigDecimal(2);

    @Inject
    private VacationStockRepository vacationStockRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVacationStockMockMvc;

    private VacationStock vacationStock;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VacationStockResource vacationStockResource = new VacationStockResource();
        ReflectionTestUtils.setField(vacationStockResource, "vacationStockRepository", vacationStockRepository);
        this.restVacationStockMockMvc = MockMvcBuilders.standaloneSetup(vacationStockResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        vacationStock = new VacationStock();
        vacationStock.setFreeDays(DEFAULT_FREE_DAYS);
    }

    @Test
    @Transactional
    public void createVacationStock() throws Exception {
        int databaseSizeBeforeCreate = vacationStockRepository.findAll().size();

        // Create the VacationStock

        restVacationStockMockMvc.perform(post("/api/vacation-stocks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vacationStock)))
                .andExpect(status().isCreated());

        // Validate the VacationStock in the database
        List<VacationStock> vacationStocks = vacationStockRepository.findAll();
        assertThat(vacationStocks).hasSize(databaseSizeBeforeCreate + 1);
        VacationStock testVacationStock = vacationStocks.get(vacationStocks.size() - 1);
        assertThat(testVacationStock.getFreeDays()).isEqualTo(DEFAULT_FREE_DAYS);
    }

    @Test
    @Transactional
    public void getAllVacationStocks() throws Exception {
        // Initialize the database
        vacationStockRepository.saveAndFlush(vacationStock);

        // Get all the vacationStocks
        restVacationStockMockMvc.perform(get("/api/vacation-stocks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(vacationStock.getId().intValue())))
                .andExpect(jsonPath("$.[*].freeDays").value(hasItem(DEFAULT_FREE_DAYS.intValue())));
    }

    @Test
    @Transactional
    public void getVacationStock() throws Exception {
        // Initialize the database
        vacationStockRepository.saveAndFlush(vacationStock);

        // Get the vacationStock
        restVacationStockMockMvc.perform(get("/api/vacation-stocks/{id}", vacationStock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(vacationStock.getId().intValue()))
            .andExpect(jsonPath("$.freeDays").value(DEFAULT_FREE_DAYS.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingVacationStock() throws Exception {
        // Get the vacationStock
        restVacationStockMockMvc.perform(get("/api/vacation-stocks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVacationStock() throws Exception {
        // Initialize the database
        vacationStockRepository.saveAndFlush(vacationStock);
        int databaseSizeBeforeUpdate = vacationStockRepository.findAll().size();

        // Update the vacationStock
        VacationStock updatedVacationStock = new VacationStock();
        updatedVacationStock.setId(vacationStock.getId());
        updatedVacationStock.setFreeDays(UPDATED_FREE_DAYS);

        restVacationStockMockMvc.perform(put("/api/vacation-stocks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedVacationStock)))
                .andExpect(status().isOk());

        // Validate the VacationStock in the database
        List<VacationStock> vacationStocks = vacationStockRepository.findAll();
        assertThat(vacationStocks).hasSize(databaseSizeBeforeUpdate);
        VacationStock testVacationStock = vacationStocks.get(vacationStocks.size() - 1);
        assertThat(testVacationStock.getFreeDays()).isEqualTo(UPDATED_FREE_DAYS);
    }

    @Test
    @Transactional
    public void deleteVacationStock() throws Exception {
        // Initialize the database
        vacationStockRepository.saveAndFlush(vacationStock);
        int databaseSizeBeforeDelete = vacationStockRepository.findAll().size();

        // Get the vacationStock
        restVacationStockMockMvc.perform(delete("/api/vacation-stocks/{id}", vacationStock.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<VacationStock> vacationStocks = vacationStockRepository.findAll();
        assertThat(vacationStocks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
