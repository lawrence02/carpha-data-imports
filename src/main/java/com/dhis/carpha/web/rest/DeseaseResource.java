package com.dhis.carpha.web.rest;

import com.dhis.carpha.domain.Desease;
import com.dhis.carpha.repository.DeseaseRepository;
import com.dhis.carpha.service.DeseaseService;
import com.dhis.carpha.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.dhis.carpha.domain.Desease}.
 */
@RestController
@RequestMapping("/api")
public class DeseaseResource {

    private final Logger log = LoggerFactory.getLogger(DeseaseResource.class);

    private static final String ENTITY_NAME = "desease";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeseaseService deseaseService;

    private final DeseaseRepository deseaseRepository;

    public DeseaseResource(DeseaseService deseaseService, DeseaseRepository deseaseRepository) {
        this.deseaseService = deseaseService;
        this.deseaseRepository = deseaseRepository;
    }

    /**
     * {@code POST  /deseases} : Create a new desease.
     *
     * @param desease the desease to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new desease, or with status {@code 400 (Bad Request)} if the desease has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deseases")
    public ResponseEntity<Desease> createDesease(@RequestBody Desease desease) throws URISyntaxException {
        log.debug("REST request to save Desease : {}", desease);
        if (desease.getId() != null) {
            throw new BadRequestAlertException("A new desease cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Desease result = deseaseService.save(desease);
        return ResponseEntity
            .created(new URI("/api/deseases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deseases/:id} : Updates an existing desease.
     *
     * @param id the id of the desease to save.
     * @param desease the desease to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated desease,
     * or with status {@code 400 (Bad Request)} if the desease is not valid,
     * or with status {@code 500 (Internal Server Error)} if the desease couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deseases/{id}")
    public ResponseEntity<Desease> updateDesease(@PathVariable(value = "id", required = false) final Long id, @RequestBody Desease desease)
        throws URISyntaxException {
        log.debug("REST request to update Desease : {}, {}", id, desease);
        if (desease.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, desease.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deseaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Desease result = deseaseService.update(desease);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, desease.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /deseases/:id} : Partial updates given fields of an existing desease, field will ignore if it is null
     *
     * @param id the id of the desease to save.
     * @param desease the desease to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated desease,
     * or with status {@code 400 (Bad Request)} if the desease is not valid,
     * or with status {@code 404 (Not Found)} if the desease is not found,
     * or with status {@code 500 (Internal Server Error)} if the desease couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/deseases/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Desease> partialUpdateDesease(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Desease desease
    ) throws URISyntaxException {
        log.debug("REST request to partial update Desease partially : {}, {}", id, desease);
        if (desease.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, desease.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deseaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Desease> result = deseaseService.partialUpdate(desease);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, desease.getId().toString())
        );
    }

    /**
     * {@code GET  /deseases} : get all the deseases.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deseases in body.
     */
    @GetMapping("/deseases")
    public ResponseEntity<List<Desease>> getAllDeseases(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Deseases");
        Page<Desease> page = deseaseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /deseases/:id} : get the "id" desease.
     *
     * @param id the id of the desease to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the desease, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deseases/{id}")
    public ResponseEntity<Desease> getDesease(@PathVariable Long id) {
        log.debug("REST request to get Desease : {}", id);
        Optional<Desease> desease = deseaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(desease);
    }

    /**
     * {@code DELETE  /deseases/:id} : delete the "id" desease.
     *
     * @param id the id of the desease to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deseases/{id}")
    public ResponseEntity<Void> deleteDesease(@PathVariable Long id) {
        log.debug("REST request to delete Desease : {}", id);
        deseaseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
