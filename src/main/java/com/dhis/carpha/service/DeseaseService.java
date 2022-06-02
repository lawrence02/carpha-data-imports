package com.dhis.carpha.service;

import com.dhis.carpha.domain.Desease;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Desease}.
 */
public interface DeseaseService {
    /**
     * Save a desease.
     *
     * @param desease the entity to save.
     * @return the persisted entity.
     */
    Desease save(Desease desease);

    /**
     * Updates a desease.
     *
     * @param desease the entity to update.
     * @return the persisted entity.
     */
    Desease update(Desease desease);

    /**
     * Partially updates a desease.
     *
     * @param desease the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Desease> partialUpdate(Desease desease);

    /**
     * Get all the deseases.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Desease> findAll(Pageable pageable);

    /**
     * Get the "id" desease.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Desease> findOne(Long id);

    /**
     * Delete the "id" desease.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
