package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A VacationRequest.
 */
@Entity
@Table(name = "vacation_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VacationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "vacation_days", precision=10, scale=2)
    private BigDecimal vacationDays;

    @Column(name = "status")
    private String status;

    @Column(name = "period")
    private ZonedDateTime period;

    @ManyToOne
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getVacationDays() {
        return vacationDays;
    }

    public void setVacationDays(BigDecimal vacationDays) {
        this.vacationDays = vacationDays;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getPeriod() {
        return period;
    }

    public void setPeriod(ZonedDateTime period) {
        this.period = period;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VacationRequest vacationRequest = (VacationRequest) o;
        if(vacationRequest.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, vacationRequest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "VacationRequest{" +
            "id=" + id +
            ", vacationDays='" + vacationDays + "'" +
            ", status='" + status + "'" +
            ", period='" + period + "'" +
            '}';
    }
}
