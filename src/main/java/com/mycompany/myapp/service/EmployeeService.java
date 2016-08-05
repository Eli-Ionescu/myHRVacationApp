package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.domain.VacationStock;
import com.mycompany.myapp.repository.EmployeeRepository;
import com.mycompany.myapp.repository.VacationStockRepository;
import com.mycompany.myapp.web.rest.VacationRequestResource;
import com.mycompany.myapp.web.rest.VacationStockResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Service Implementation for managing Employee.
 */
@Service
@Transactional
public class EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private VacationStockRepository vacationStockRepository;

    /**
     * Save a employee.
     *
     * @param employee the entity to save
     * @return the persisted entity
     */
    public Employee save(Employee employee) {
        log.debug("Request to save Employee : {}", employee);

        // TODO 1 : Adaugare autoamata stoc concediu la adaugare angajat
        // TODO 2 : Verificare existenta stooc concediu angajat
        if(employee.getVacationStockId() == null){
            // TODO 3 : Generarezilelor de concediu in functi de data de angajare

            /*
                Folosesc fomrula :
                Zile concediu =  1.75 * luni de munca din an
                luni de munca din an =  12 - luna angajarii
             */
            Integer workingMonths = 12 - employee.getHireDate().getMonthValue();
            BigDecimal vacantionDays = BigDecimal.valueOf(1.75 * workingMonths);
            VacationStock vacationStock = new VacationStock();
            vacationStock.setFreeDays(vacantionDays);

            vacationStock = vacationStockRepository.save(vacationStock);
            employee.setVacationStockId(vacationStock);
       }

        Employee result = employeeRepository.save(employee);
        return result;
    }

    /**
     *  Get all the employees.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Employee> findAll(Pageable pageable) {
        log.debug("Request to get all Employees");
        Page<Employee> result = employeeRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one employee by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Employee findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        Employee employee = employeeRepository.findOneWithEagerRelationships(id);
        return employee;
    }

    /**
     *  Delete the  employee by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        employeeRepository.delete(id);
    }
}
