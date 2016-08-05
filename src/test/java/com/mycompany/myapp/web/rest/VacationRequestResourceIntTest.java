package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MyHrAppConcediuApp;
import com.mycompany.myapp.domain.VacationRequest;
import com.mycompany.myapp.repository.VacationRequestRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the VacationRequestResource REST controller.
 *
 * @see VacationRequestResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyHrAppConcediuApp.class)
@WebAppConfiguration
@IntegrationTest
public class VacationRequestResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final BigDecimal DEFAULT_VACATION_DAYS = new BigDecimal(1);
    private static final BigDecimal UPDATED_VACATION_DAYS = new BigDecimal(2);
    private static final String DEFAULT_STATUS = "AAAAA";
    private static final String UPDATED_STATUS = "BBBBB";

    private static final ZonedDateTime DEFAULT_PERIOD = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_PERIOD = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_PERIOD_STR = dateTimeFormatter.format(DEFAULT_PERIOD);

    @Inject
    private VacationRequestRepository vacationRequestRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVacationRequestMockMvc;

    private VacationRequest vacationRequest;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VacationRequestResource vacationRequestResource = new VacationRequestResource();
        ReflectionTestUtils.setField(vacationRequestResource, "vacationRequestRepository", vacationRequestRepository);
        this.restVacationRequestMockMvc = MockMvcBuilders.standaloneSetup(vacationRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        vacationRequest = new VacationRequest();
        vacationRequest.setVacationDays(DEFAULT_VACATION_DAYS);
        vacationRequest.setStatus(DEFAULT_STATUS);
        vacationRequest.setPeriod(DEFAULT_PERIOD);
    }

    @Test
    @Transactional
    public void createVacationRequest() throws Exception {
        int databaseSizeBeforeCreate = vacationRequestRepository.findAll().size();

        // Create the VacationRequest

        restVacationRequestMockMvc.perform(post("/api/vacation-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vacationRequest)))
                .andExpect(status().isCreated());

        // Validate the VacationRequest in the database
        List<VacationRequest> vacationRequests = vacationRequestRepository.findAll();
        assertThat(vacationRequests).hasSize(databaseSizeBeforeCreate + 1);
        VacationRequest testVacationRequest = vacationRequests.get(vacationRequests.size() - 1);
        assertThat(testVacationRequest.getVacationDays()).isEqualTo(DEFAULT_VACATION_DAYS);
        assertThat(testVacationRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testVacationRequest.getPeriod()).isEqualTo(DEFAULT_PERIOD);
    }

    @Test
    @Transactional
    public void getAllVacationRequests() throws Exception {
        // Initialize the database
        vacationRequestRepository.saveAndFlush(vacationRequest);

        // Get all the vacationRequests
        restVacationRequestMockMvc.perform(get("/api/vacation-requests?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(vacationRequest.getId().intValue())))
                .andExpect(jsonPath("$.[*].vacationDays").value(hasItem(DEFAULT_VACATION_DAYS.intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD_STR)));
    }

    @Test
    @Transactional
    public void getVacationRequest() throws Exception {
        // Initialize the database
        vacationRequestRepository.saveAndFlush(vacationRequest);

        // Get the vacationRequest
        restVacationRequestMockMvc.perform(get("/api/vacation-requests/{id}", vacationRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(vacationRequest.getId().intValue()))
            .andExpect(jsonPath("$.vacationDays").value(DEFAULT_VACATION_DAYS.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.period").value(DEFAULT_PERIOD_STR));
    }

    @Test
    @Transactional
    public void getNonExistingVacationRequest() throws Exception {
        // Get the vacationRequest
        restVacationRequestMockMvc.perform(get("/api/vacation-requests/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVacationRequest() throws Exception {
        // Initialize the database
        vacationRequestRepository.saveAndFlush(vacationRequest);
        int databaseSizeBeforeUpdate = vacationRequestRepository.findAll().size();

        // Update the vacationRequest
        VacationRequest updatedVacationRequest = new VacationRequest();
        updatedVacationRequest.setId(vacationRequest.getId());
        updatedVacationRequest.setVacationDays(UPDATED_VACATION_DAYS);
        updatedVacationRequest.setStatus(UPDATED_STATUS);
        updatedVacationRequest.setPeriod(UPDATED_PERIOD);

        restVacationRequestMockMvc.perform(put("/api/vacation-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedVacationRequest)))
                .andExpect(status().isOk());

        // Validate the VacationRequest in the database
        List<VacationRequest> vacationRequests = vacationRequestRepository.findAll();
        assertThat(vacationRequests).hasSize(databaseSizeBeforeUpdate);
        VacationRequest testVacationRequest = vacationRequests.get(vacationRequests.size() - 1);
        assertThat(testVacationRequest.getVacationDays()).isEqualTo(UPDATED_VACATION_DAYS);
        assertThat(testVacationRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testVacationRequest.getPeriod()).isEqualTo(UPDATED_PERIOD);
    }

    @Test
    @Transactional
    public void deleteVacationRequest() throws Exception {
        // Initialize the database
        vacationRequestRepository.saveAndFlush(vacationRequest);
        int databaseSizeBeforeDelete = vacationRequestRepository.findAll().size();

        // Get the vacationRequest
        restVacationRequestMockMvc.perform(delete("/api/vacation-requests/{id}", vacationRequest.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<VacationRequest> vacationRequests = vacationRequestRepository.findAll();
        assertThat(vacationRequests).hasSize(databaseSizeBeforeDelete - 1);
    }
}
