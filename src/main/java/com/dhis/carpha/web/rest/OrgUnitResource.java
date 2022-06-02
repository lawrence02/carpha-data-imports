package com.dhis.carpha.web.rest;

import com.dhis.carpha.domain.OrgUnit;
import com.dhis.carpha.repository.OrgUnitRepository;
import com.dhis.carpha.service.OrgUnitService;
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
 * REST controller for managing {@link com.dhis.carpha.domain.OrgUnit}.
 */
@RestController
@RequestMapping("/api")
public class OrgUnitResource {

    private final Logger log = LoggerFactory.getLogger(OrgUnitResource.class);

    private static final String ENTITY_NAME = "orgUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrgUnitService orgUnitService;

    private final OrgUnitRepository orgUnitRepository;

    public OrgUnitResource(OrgUnitService orgUnitService, OrgUnitRepository orgUnitRepository) {
        this.orgUnitService = orgUnitService;
        this.orgUnitRepository = orgUnitRepository;
    }

    /**
     * {@code POST  /org-units} : Create a new orgUnit.
     *
     * @param orgUnit the orgUnit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orgUnit, or with status {@code 400 (Bad Request)} if the orgUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/org-units")
    public ResponseEntity<OrgUnit> createOrgUnit(@RequestBody OrgUnit orgUnit) throws URISyntaxException {
        log.debug("REST request to save OrgUnit : {}", orgUnit);
        if (orgUnit.getId() != null) {
            throw new BadRequestAlertException("A new orgUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrgUnit result = orgUnitService.save(orgUnit);
        return ResponseEntity
            .created(new URI("/api/org-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /org-units/:id} : Updates an existing orgUnit.
     *
     * @param id the id of the orgUnit to save.
     * @param orgUnit the orgUnit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orgUnit,
     * or with status {@code 400 (Bad Request)} if the orgUnit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orgUnit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/org-units/{id}")
    public ResponseEntity<OrgUnit> updateOrgUnit(@PathVariable(value = "id", required = false) final Long id, @RequestBody OrgUnit orgUnit)
        throws URISyntaxException {
        log.debug("REST request to update OrgUnit : {}, {}", id, orgUnit);
        if (orgUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orgUnit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrgUnit result = orgUnitService.update(orgUnit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orgUnit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /org-units/:id} : Partial updates given fields of an existing orgUnit, field will ignore if it is null
     *
     * @param id the id of the orgUnit to save.
     * @param orgUnit the orgUnit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orgUnit,
     * or with status {@code 400 (Bad Request)} if the orgUnit is not valid,
     * or with status {@code 404 (Not Found)} if the orgUnit is not found,
     * or with status {@code 500 (Internal Server Error)} if the orgUnit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/org-units/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrgUnit> partialUpdateOrgUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrgUnit orgUnit
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrgUnit partially : {}, {}", id, orgUnit);
        if (orgUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orgUnit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrgUnit> result = orgUnitService.partialUpdate(orgUnit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orgUnit.getId().toString())
        );
    }

    /**
     * {@code GET  /org-units} : get all the orgUnits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orgUnits in body.
     */
    @GetMapping("/org-units")
    public ResponseEntity<List<OrgUnit>> getAllOrgUnits(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of OrgUnits");
        Page<OrgUnit> page = orgUnitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /org-units/:id} : get the "id" orgUnit.
     *
     * @param id the id of the orgUnit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orgUnit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/org-units/{id}")
    public ResponseEntity<OrgUnit> getOrgUnit(@PathVariable Long id) {
        log.debug("REST request to get OrgUnit : {}", id);
        Optional<OrgUnit> orgUnit = orgUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orgUnit);
    }

    /**
     * {@code DELETE  /org-units/:id} : delete the "id" orgUnit.
     *
     * @param id the id of the orgUnit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/org-units/{id}")
    public ResponseEntity<Void> deleteOrgUnit(@PathVariable Long id) {
        log.debug("REST request to delete OrgUnit : {}", id);
        orgUnitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
