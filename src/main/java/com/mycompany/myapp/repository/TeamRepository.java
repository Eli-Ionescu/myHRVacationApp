package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Lion King on 11-Aug-16.
 */
public interface TeamRepository extends JpaRepository<Team, Long>{
}
