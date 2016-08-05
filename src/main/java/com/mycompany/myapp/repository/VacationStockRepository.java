package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.VacationStock;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the VacationStock entity.
 */
@SuppressWarnings("unused")
public interface VacationStockRepository extends JpaRepository<VacationStock,Long> {

}
