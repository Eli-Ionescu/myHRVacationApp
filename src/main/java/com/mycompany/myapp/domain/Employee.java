package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * The Employee entity.                                                        
 * 
 */
@ApiModel(description = ""
    + "The Employee entity.                                                   "
    + "")
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "hire_date")
    private ZonedDateTime hireDate;

    @Column(name = "salary")
    private Long salary;

    @Column(name = "commission_pct")
    private Long commissionPct;

    @OneToOne
    @JoinColumn(unique = true)
    private VacationStock vacationStockId;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<VacationRequest> vacationRequestIds = new HashSet<>();

    @ManyToOne
    private Job jobId;

    @ManyToOne
    private Department departmentId;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "employee_manager_id",
               joinColumns = @JoinColumn(name="employees_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="manager_ids_id", referencedColumnName="ID"))
    private Set<Employee> managerIds = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ZonedDateTime getHireDate() {
        return hireDate;
    }

    public void setHireDate(ZonedDateTime hireDate) {
        this.hireDate = hireDate;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Long getCommissionPct() {
        return commissionPct;
    }

    public void setCommissionPct(Long commissionPct) {
        this.commissionPct = commissionPct;
    }

    public VacationStock getVacationStockId() {
        return vacationStockId;
    }

    public void setVacationStockId(VacationStock vacationStock) {
        this.vacationStockId = vacationStock;
    }

    public Set<VacationRequest> getVacationRequestIds() {
        return vacationRequestIds;
    }

    public void setVacationRequestIds(Set<VacationRequest> vacationRequests) {
        this.vacationRequestIds = vacationRequests;
    }

    public Job getJobId() {
        return jobId;
    }

    public void setJobId(Job job) {
        this.jobId = job;
    }

    public Department getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Department department) {
        this.departmentId = department;
    }

    public Set<Employee> getManagerIds() {
        return managerIds;
    }

    public void setManagerIds(Set<Employee> employees) {
        this.managerIds = employees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        if(employee.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Employee{" +
            "id=" + id +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", email='" + email + "'" +
            ", phoneNumber='" + phoneNumber + "'" +
            ", hireDate='" + hireDate + "'" +
            ", salary='" + salary + "'" +
            ", commissionPct='" + commissionPct + "'" +
            '}';
    }
}
