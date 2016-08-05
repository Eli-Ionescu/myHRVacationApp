package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A VacationStock.
 */
@Entity
@Table(name = "vacation_stock")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VacationStock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "free_days", precision=10, scale=2)
    private BigDecimal freeDays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getFreeDays() {
        return freeDays;
    }

    public void setFreeDays(BigDecimal freeDays) {
        this.freeDays = freeDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VacationStock vacationStock = (VacationStock) o;
        if(vacationStock.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, vacationStock.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "VacationStock{" +
            "id=" + id +
            ", freeDays='" + freeDays + "'" +
            '}';
    }
}
