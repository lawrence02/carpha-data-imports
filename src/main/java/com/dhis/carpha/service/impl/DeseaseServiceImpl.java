package com.dhis.carpha.service.impl;

import com.dhis.carpha.domain.Desease;
import com.dhis.carpha.repository.DeseaseRepository;
import com.dhis.carpha.service.DeseaseService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Desease}.
 */
@Service
@Transactional
public class DeseaseServiceImpl implements DeseaseService {

    private final Logger log = LoggerFactory.getLogger(DeseaseServiceImpl.class);

    private final DeseaseRepository deseaseRepository;

    public DeseaseServiceImpl(DeseaseRepository deseaseRepository) {
        this.deseaseRepository = deseaseRepository;
    }

    @Override
    public Desease save(Desease desease) {
        log.debug("Request to save Desease : {}", desease);
        return deseaseRepository.save(desease);
    }

    @Override
    public Desease update(Desease desease) {
        log.debug("Request to save Desease : {}", desease);
        return deseaseRepository.save(desease);
    }

    @Override
    public Optional<Desease> partialUpdate(Desease desease) {
        log.debug("Request to partially update Desease : {}", desease);

        return deseaseRepository
            .findById(desease.getId())
            .map(existingDesease -> {
                if (desease.getCountry() != null) {
                    existingDesease.setCountry(desease.getCountry());
                }
                if (desease.getDeseaseId() != null) {
                    existingDesease.setDeseaseId(desease.getDeseaseId());
                }
                if (desease.getCaseInfo() != null) {
                    existingDesease.setCaseInfo(desease.getCaseInfo());
                }
                if (desease.getYear() != null) {
                    existingDesease.setYear(desease.getYear());
                }
                if (desease.getWeek() != null) {
                    existingDesease.setWeek(desease.getWeek());
                }
                if (desease.getWeekEnding() != null) {
                    existingDesease.setWeekEnding(desease.getWeekEnding());
                }

                return existingDesease;
            })
            .map(deseaseRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Desease> findAll(Pageable pageable) {
        log.debug("Request to get all Deseases");
        return deseaseRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Desease> findOne(Long id) {
        log.debug("Request to get Desease : {}", id);
        return deseaseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Desease : {}", id);
        deseaseRepository.deleteById(id);
    }
}
