package com.dhis.carpha.repository;

import com.dhis.carpha.domain.OrgUnit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrgUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrgUnitRepository extends JpaRepository<OrgUnit, Long> {}
