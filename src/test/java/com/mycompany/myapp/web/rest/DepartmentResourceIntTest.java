package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MyHrAppConcediuApp;
import com.mycompany.myapp.domain.Department;
import com.mycompany.myapp.repository.DepartmentRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DepartmentResource REST controller.
 *
 * @see DepartmentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyHrAppConcediuApp.class)
@WebAppConfiguration
@IntegrationTest
public class DepartmentResourceIntTest {

    private static final String DEFAULT_DEPARTMENT_NAME = "AAAAA";
    private static final String UPDATED_DEPARTMENT_NAME = "BBBBB";

    private static final Long DEFAULT_LOCATION_ID = 1L;
    private static final Long UPDATED_LOCATION_ID = 2L;

    @Inject
    private DepartmentRepository departmentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDepartmentMockMvc;

    private Department department;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DepartmentResource departmentResource = new DepartmentResource();
        ReflectionTestUtils.setField(departmentResource, "departmentRepository", departmentRepository);
        this.restDepartmentMockMvc = MockMvcBuilders.standaloneSetup(departmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        department = new Department();
        department.setDepartmentName(DEFAULT_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    public void createDepartment() throws Exception {
        int databaseSizeBeforeCreate = departmentRepository.findAll().size();

        // Create the Department

        restDepartmentMockMvc.perform(post("/api/departments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(department)))
                .andExpect(status().isCreated());

        // Validate the Department in the database
        List<Department> departments = departmentRepository.findAll();
        assertThat(departments).hasSize(databaseSizeBeforeCreate + 1);
        Department testDepartment = departments.get(departments.size() - 1);
        assertThat(testDepartment.getDepartmentName()).isEqualTo(DEFAULT_DEPARTMENT_NAME);
        assertThat(testDepartment.getLocationId()).isEqualTo(DEFAULT_LOCATION_ID);
    }

    @Test
    @Transactional
    public void getAllDepartments() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departments
        restDepartmentMockMvc.perform(get("/api/departments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(department.getId().intValue())))
                .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME.toString())))
                .andExpect(jsonPath("$.[*].locationId").value(hasItem(DEFAULT_LOCATION_ID.intValue())));
    }

    @Test
    @Transactional
    public void getDepartment() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get the department
        restDepartmentMockMvc.perform(get("/api/departments/{id}", department.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(department.getId().intValue()))
            .andExpect(jsonPath("$.departmentName").value(DEFAULT_DEPARTMENT_NAME.toString()))
            .andExpect(jsonPath("$.locationId").value(DEFAULT_LOCATION_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDepartment() throws Exception {
        // Get the department
        restDepartmentMockMvc.perform(get("/api/departments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepartment() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);
        int databaseSizeBeforeUpdate = departmentRepository.findAll().size();

        // Update the department
        Department updatedDepartment = new Department();
        updatedDepartment.setId(department.getId());
        updatedDepartment.setDepartmentName(UPDATED_DEPARTMENT_NAME);

        restDepartmentMockMvc.perform(put("/api/departments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDepartment)))
                .andExpect(status().isOk());

        // Validate the Department in the database
        List<Department> departments = departmentRepository.findAll();
        assertThat(departments).hasSize(databaseSizeBeforeUpdate);
        Department testDepartment = departments.get(departments.size() - 1);
        assertThat(testDepartment.getDepartmentName()).isEqualTo(UPDATED_DEPARTMENT_NAME);
        assertThat(testDepartment.getLocationId()).isEqualTo(UPDATED_LOCATION_ID);
    }

    @Test
    @Transactional
    public void deleteDepartment() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);
        int databaseSizeBeforeDelete = departmentRepository.findAll().size();

        // Get the department
        restDepartmentMockMvc.perform(delete("/api/departments/{id}", department.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Department> departments = departmentRepository.findAll();
        assertThat(departments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
