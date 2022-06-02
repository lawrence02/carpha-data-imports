package com.dhis.carpha.service.impl;

import com.dhis.carpha.domain.OrgUnit;
import com.dhis.carpha.repository.OrgUnitRepository;
import com.dhis.carpha.service.OrgUnitService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrgUnit}.
 */
@Service
@Transactional
public class OrgUnitServiceImpl implements OrgUnitService {

    private final Logger log = LoggerFactory.getLogger(OrgUnitServiceImpl.class);

    private final OrgUnitRepository orgUnitRepository;

    public OrgUnitServiceImpl(OrgUnitRepository orgUnitRepository) {
        this.orgUnitRepository = orgUnitRepository;
    }

    @Override
    public OrgUnit save(OrgUnit orgUnit) {
        log.debug("Request to save OrgUnit : {}", orgUnit);
        return orgUnitRepository.save(orgUnit);
    }

    @Override
    public OrgUnit update(OrgUnit orgUnit) {
        log.debug("Request to save OrgUnit : {}", orgUnit);
        return orgUnitRepository.save(orgUnit);
    }

    @Override
    public Optional<OrgUnit> partialUpdate(OrgUnit orgUnit) {
        log.debug("Request to partially update OrgUnit : {}", orgUnit);

        return orgUnitRepository
            .findById(orgUnit.getId())
            .map(existingOrgUnit -> {
                if (orgUnit.getOrgUnitName() != null) {
                    existingOrgUnit.setOrgUnitName(orgUnit.getOrgUnitName());
                }
                if (orgUnit.getDhisOrgUnitName() != null) {
                    existingOrgUnit.setDhisOrgUnitName(orgUnit.getDhisOrgUnitName());
                }
                if (orgUnit.getDhisOrgUnitCode() != null) {
                    existingOrgUnit.setDhisOrgUnitCode(orgUnit.getDhisOrgUnitCode());
                }

                return existingOrgUnit;
            })
            .map(orgUnitRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrgUnit> findAll(Pageable pageable) {
        log.debug("Request to get all OrgUnits");
        return orgUnitRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrgUnit> findOne(Long id) {
        log.debug("Request to get OrgUnit : {}", id);
        return orgUnitRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrgUnit : {}", id);
        orgUnitRepository.deleteById(id);
    }
}
