package com.dhis.carpha.repository;

import com.dhis.carpha.domain.Desease;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Desease entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeseaseRepository extends JpaRepository<Desease, Long> {}
