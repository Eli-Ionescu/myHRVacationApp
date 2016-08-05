package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.VacationRequest;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the VacationRequest entity.
 */
@SuppressWarnings("unused")
public interface VacationRequestRepository extends JpaRepository<VacationRequest,Long> {

}
