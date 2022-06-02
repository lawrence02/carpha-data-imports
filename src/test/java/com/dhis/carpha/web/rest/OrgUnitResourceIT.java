package com.dhis.carpha.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dhis.carpha.IntegrationTest;
import com.dhis.carpha.domain.OrgUnit;
import com.dhis.carpha.repository.OrgUnitRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrgUnitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrgUnitResourceIT {

    private static final String DEFAULT_ORG_UNIT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORG_UNIT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DHIS_ORG_UNIT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DHIS_ORG_UNIT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DHIS_ORG_UNIT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DHIS_ORG_UNIT_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/org-units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrgUnitRepository orgUnitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrgUnitMockMvc;

    private OrgUnit orgUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgUnit createEntity(EntityManager em) {
        OrgUnit orgUnit = new OrgUnit()
            .orgUnitName(DEFAULT_ORG_UNIT_NAME)
            .dhisOrgUnitName(DEFAULT_DHIS_ORG_UNIT_NAME)
            .dhisOrgUnitCode(DEFAULT_DHIS_ORG_UNIT_CODE);
        return orgUnit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgUnit createUpdatedEntity(EntityManager em) {
        OrgUnit orgUnit = new OrgUnit()
            .orgUnitName(UPDATED_ORG_UNIT_NAME)
            .dhisOrgUnitName(UPDATED_DHIS_ORG_UNIT_NAME)
            .dhisOrgUnitCode(UPDATED_DHIS_ORG_UNIT_CODE);
        return orgUnit;
    }

    @BeforeEach
    public void initTest() {
        orgUnit = createEntity(em);
    }

    @Test
    @Transactional
    void createOrgUnit() throws Exception {
        int databaseSizeBeforeCreate = orgUnitRepository.findAll().size();
        // Create the OrgUnit
        restOrgUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgUnit)))
            .andExpect(status().isCreated());

        // Validate the OrgUnit in the database
        List<OrgUnit> orgUnitList = orgUnitRepository.findAll();
        assertThat(orgUnitList).hasSize(databaseSizeBeforeCreate + 1);
        OrgUnit testOrgUnit = orgUnitList.get(orgUnitList.size() - 1);
        assertThat(testOrgUnit.getOrgUnitName()).isEqualTo(DEFAULT_ORG_UNIT_NAME);
        assertThat(testOrgUnit.getDhisOrgUnitName()).isEqualTo(DEFAULT_DHIS_ORG_UNIT_NAME);
        assertThat(testOrgUnit.getDhisOrgUnitCode()).isEqualTo(DEFAULT_DHIS_ORG_UNIT_CODE);
    }

    @Test
    @Transactional
    void createOrgUnitWithExistingId() throws Exception {
        // Create the OrgUnit with an existing ID
        orgUnit.setId(1L);

        int databaseSizeBeforeCreate = orgUnitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrgUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgUnit)))
            .andExpect(status().isBadRequest());

        // Validate the OrgUnit in the database
        List<OrgUnit> orgUnitList = orgUnitRepository.findAll();
        assertThat(orgUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrgUnits() throws Exception {
        // Initialize the database
        orgUnitRepository.saveAndFlush(orgUnit);

        // Get all the orgUnitList
        restOrgUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].orgUnitName").value(hasItem(DEFAULT_ORG_UNIT_NAME)))
            .andExpect(jsonPath("$.[*].dhisOrgUnitName").value(hasItem(DEFAULT_DHIS_ORG_UNIT_NAME)))
            .andExpect(jsonPath("$.[*].dhisOrgUnitCode").value(hasItem(DEFAULT_DHIS_ORG_UNIT_CODE)));
    }

    @Test
    @Transactional
    void getOrgUnit() throws Exception {
        // Initialize the database
        orgUnitRepository.saveAndFlush(orgUnit);

        // Get the orgUnit
        restOrgUnitMockMvc
            .perform(get(ENTITY_API_URL_ID, orgUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orgUnit.getId().intValue()))
            .andExpect(jsonPath("$.orgUnitName").value(DEFAULT_ORG_UNIT_NAME))
            .andExpect(jsonPath("$.dhisOrgUnitName").value(DEFAULT_DHIS_ORG_UNIT_NAME))
            .andExpect(jsonPath("$.dhisOrgUnitCode").value(DEFAULT_DHIS_ORG_UNIT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingOrgUnit() throws Exception {
        // Get the orgUnit
        restOrgUnitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrgUnit() throws Exception {
        // Initialize the database
        orgUnitRepository.saveAndFlush(orgUnit);

        int databaseSizeBeforeUpdate = orgUnitRepository.findAll().size();

        // Update the orgUnit
        OrgUnit updatedOrgUnit = orgUnitRepository.findById(orgUnit.getId()).get();
        // Disconnect from session so that the updates on updatedOrgUnit are not directly saved in db
        em.detach(updatedOrgUnit);
        updatedOrgUnit
            .orgUnitName(UPDATED_ORG_UNIT_NAME)
            .dhisOrgUnitName(UPDATED_DHIS_ORG_UNIT_NAME)
            .dhisOrgUnitCode(UPDATED_DHIS_ORG_UNIT_CODE);

        restOrgUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrgUnit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrgUnit))
            )
            .andExpect(status().isOk());

        // Validate the OrgUnit in the database
        List<OrgUnit> orgUnitList = orgUnitRepository.findAll();
        assertThat(orgUnitList).hasSize(databaseSizeBeforeUpdate);
        OrgUnit testOrgUnit = orgUnitList.get(orgUnitList.size() - 1);
        assertThat(testOrgUnit.getOrgUnitName()).isEqualTo(UPDATED_ORG_UNIT_NAME);
        assertThat(testOrgUnit.getDhisOrgUnitName()).isEqualTo(UPDATED_DHIS_ORG_UNIT_NAME);
        assertThat(testOrgUnit.getDhisOrgUnitCode()).isEqualTo(UPDATED_DHIS_ORG_UNIT_CODE);
    }

    @Test
    @Transactional
    void putNonExistingOrgUnit() throws Exception {
        int databaseSizeBeforeUpdate = orgUnitRepository.findAll().size();
        orgUnit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orgUnit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orgUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgUnit in the database
        List<OrgUnit> orgUnitList = orgUnitRepository.findAll();
        assertThat(orgUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrgUnit() throws Exception {
        int databaseSizeBeforeUpdate = orgUnitRepository.findAll().size();
        orgUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orgUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgUnit in the database
        List<OrgUnit> orgUnitList = orgUnitRepository.findAll();
        assertThat(orgUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrgUnit() throws Exception {
        int databaseSizeBeforeUpdate = orgUnitRepository.findAll().size();
        orgUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUnitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgUnit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgUnit in the database
        List<OrgUnit> orgUnitList = orgUnitRepository.findAll();
        assertThat(orgUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrgUnitWithPatch() throws Exception {
        // Initialize the database
        orgUnitRepository.saveAndFlush(orgUnit);

        int databaseSizeBeforeUpdate = orgUnitRepository.findAll().size();

        // Update the orgUnit using partial update
        OrgUnit partialUpdatedOrgUnit = new OrgUnit();
        partialUpdatedOrgUnit.setId(orgUnit.getId());

        partialUpdatedOrgUnit.dhisOrgUnitName(UPDATED_DHIS_ORG_UNIT_NAME).dhisOrgUnitCode(UPDATED_DHIS_ORG_UNIT_CODE);

        restOrgUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrgUnit))
            )
            .andExpect(status().isOk());

        // Validate the OrgUnit in the database
        List<OrgUnit> orgUnitList = orgUnitRepository.findAll();
        assertThat(orgUnitList).hasSize(databaseSizeBeforeUpdate);
        OrgUnit testOrgUnit = orgUnitList.get(orgUnitList.size() - 1);
        assertThat(testOrgUnit.getOrgUnitName()).isEqualTo(DEFAULT_ORG_UNIT_NAME);
        assertThat(testOrgUnit.getDhisOrgUnitName()).isEqualTo(UPDATED_DHIS_ORG_UNIT_NAME);
        assertThat(testOrgUnit.getDhisOrgUnitCode()).isEqualTo(UPDATED_DHIS_ORG_UNIT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateOrgUnitWithPatch() throws Exception {
        // Initialize the database
        orgUnitRepository.saveAndFlush(orgUnit);

        int databaseSizeBeforeUpdate = orgUnitRepository.findAll().size();

        // Update the orgUnit using partial update
        OrgUnit partialUpdatedOrgUnit = new OrgUnit();
        partialUpdatedOrgUnit.setId(orgUnit.getId());

        partialUpdatedOrgUnit
            .orgUnitName(UPDATED_ORG_UNIT_NAME)
            .dhisOrgUnitName(UPDATED_DHIS_ORG_UNIT_NAME)
            .dhisOrgUnitCode(UPDATED_DHIS_ORG_UNIT_CODE);

        restOrgUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrgUnit))
            )
            .andExpect(status().isOk());

        // Validate the OrgUnit in the database
        List<OrgUnit> orgUnitList = orgUnitRepository.findAll();
        assertThat(orgUnitList).hasSize(databaseSizeBeforeUpdate);
        OrgUnit testOrgUnit = orgUnitList.get(orgUnitList.size() - 1);
        assertThat(testOrgUnit.getOrgUnitName()).isEqualTo(UPDATED_ORG_UNIT_NAME);
        assertThat(testOrgUnit.getDhisOrgUnitName()).isEqualTo(UPDATED_DHIS_ORG_UNIT_NAME);
        assertThat(testOrgUnit.getDhisOrgUnitCode()).isEqualTo(UPDATED_DHIS_ORG_UNIT_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingOrgUnit() throws Exception {
        int databaseSizeBeforeUpdate = orgUnitRepository.findAll().size();
        orgUnit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orgUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orgUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgUnit in the database
        List<OrgUnit> orgUnitList = orgUnitRepository.findAll();
        assertThat(orgUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrgUnit() throws Exception {
        int databaseSizeBeforeUpdate = orgUnitRepository.findAll().size();
        orgUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orgUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgUnit in the database
        List<OrgUnit> orgUnitList = orgUnitRepository.findAll();
        assertThat(orgUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrgUnit() throws Exception {
        int databaseSizeBeforeUpdate = orgUnitRepository.findAll().size();
        orgUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUnitMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orgUnit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgUnit in the database
        List<OrgUnit> orgUnitList = orgUnitRepository.findAll();
        assertThat(orgUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrgUnit() throws Exception {
        // Initialize the database
        orgUnitRepository.saveAndFlush(orgUnit);

        int databaseSizeBeforeDelete = orgUnitRepository.findAll().size();

        // Delete the orgUnit
        restOrgUnitMockMvc
            .perform(delete(ENTITY_API_URL_ID, orgUnit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrgUnit> orgUnitList = orgUnitRepository.findAll();
        assertThat(orgUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
