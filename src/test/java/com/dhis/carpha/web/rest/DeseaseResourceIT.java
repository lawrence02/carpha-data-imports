package com.dhis.carpha.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dhis.carpha.IntegrationTest;
import com.dhis.carpha.domain.Desease;
import com.dhis.carpha.repository.DeseaseRepository;
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
 * Integration tests for the {@link DeseaseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeseaseResourceIT {

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_DESEASE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DESEASE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CASE_INFO = "AAAAAAAAAA";
    private static final String UPDATED_CASE_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_YEAR = "BBBBBBBBBB";

    private static final String DEFAULT_WEEK = "AAAAAAAAAA";
    private static final String UPDATED_WEEK = "BBBBBBBBBB";

    private static final String DEFAULT_WEEK_ENDING = "AAAAAAAAAA";
    private static final String UPDATED_WEEK_ENDING = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/deseases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeseaseRepository deseaseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeseaseMockMvc;

    private Desease desease;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Desease createEntity(EntityManager em) {
        Desease desease = new Desease()
            .country(DEFAULT_COUNTRY)
            .deseaseId(DEFAULT_DESEASE_ID)
            .caseInfo(DEFAULT_CASE_INFO)
            .year(DEFAULT_YEAR)
            .week(DEFAULT_WEEK)
            .weekEnding(DEFAULT_WEEK_ENDING);
        return desease;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Desease createUpdatedEntity(EntityManager em) {
        Desease desease = new Desease()
            .country(UPDATED_COUNTRY)
            .deseaseId(UPDATED_DESEASE_ID)
            .caseInfo(UPDATED_CASE_INFO)
            .year(UPDATED_YEAR)
            .week(UPDATED_WEEK)
            .weekEnding(UPDATED_WEEK_ENDING);
        return desease;
    }

    @BeforeEach
    public void initTest() {
        desease = createEntity(em);
    }

    @Test
    @Transactional
    void createDesease() throws Exception {
        int databaseSizeBeforeCreate = deseaseRepository.findAll().size();
        // Create the Desease
        restDeseaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(desease)))
            .andExpect(status().isCreated());

        // Validate the Desease in the database
        List<Desease> deseaseList = deseaseRepository.findAll();
        assertThat(deseaseList).hasSize(databaseSizeBeforeCreate + 1);
        Desease testDesease = deseaseList.get(deseaseList.size() - 1);
        assertThat(testDesease.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testDesease.getDeseaseId()).isEqualTo(DEFAULT_DESEASE_ID);
        assertThat(testDesease.getCaseInfo()).isEqualTo(DEFAULT_CASE_INFO);
        assertThat(testDesease.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testDesease.getWeek()).isEqualTo(DEFAULT_WEEK);
        assertThat(testDesease.getWeekEnding()).isEqualTo(DEFAULT_WEEK_ENDING);
    }

    @Test
    @Transactional
    void createDeseaseWithExistingId() throws Exception {
        // Create the Desease with an existing ID
        desease.setId(1L);

        int databaseSizeBeforeCreate = deseaseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeseaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(desease)))
            .andExpect(status().isBadRequest());

        // Validate the Desease in the database
        List<Desease> deseaseList = deseaseRepository.findAll();
        assertThat(deseaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDeseases() throws Exception {
        // Initialize the database
        deseaseRepository.saveAndFlush(desease);

        // Get all the deseaseList
        restDeseaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(desease.getId().intValue())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].deseaseId").value(hasItem(DEFAULT_DESEASE_ID)))
            .andExpect(jsonPath("$.[*].caseInfo").value(hasItem(DEFAULT_CASE_INFO)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].week").value(hasItem(DEFAULT_WEEK)))
            .andExpect(jsonPath("$.[*].weekEnding").value(hasItem(DEFAULT_WEEK_ENDING)));
    }

    @Test
    @Transactional
    void getDesease() throws Exception {
        // Initialize the database
        deseaseRepository.saveAndFlush(desease);

        // Get the desease
        restDeseaseMockMvc
            .perform(get(ENTITY_API_URL_ID, desease.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(desease.getId().intValue()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.deseaseId").value(DEFAULT_DESEASE_ID))
            .andExpect(jsonPath("$.caseInfo").value(DEFAULT_CASE_INFO))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.week").value(DEFAULT_WEEK))
            .andExpect(jsonPath("$.weekEnding").value(DEFAULT_WEEK_ENDING));
    }

    @Test
    @Transactional
    void getNonExistingDesease() throws Exception {
        // Get the desease
        restDeseaseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDesease() throws Exception {
        // Initialize the database
        deseaseRepository.saveAndFlush(desease);

        int databaseSizeBeforeUpdate = deseaseRepository.findAll().size();

        // Update the desease
        Desease updatedDesease = deseaseRepository.findById(desease.getId()).get();
        // Disconnect from session so that the updates on updatedDesease are not directly saved in db
        em.detach(updatedDesease);
        updatedDesease
            .country(UPDATED_COUNTRY)
            .deseaseId(UPDATED_DESEASE_ID)
            .caseInfo(UPDATED_CASE_INFO)
            .year(UPDATED_YEAR)
            .week(UPDATED_WEEK)
            .weekEnding(UPDATED_WEEK_ENDING);

        restDeseaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDesease.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDesease))
            )
            .andExpect(status().isOk());

        // Validate the Desease in the database
        List<Desease> deseaseList = deseaseRepository.findAll();
        assertThat(deseaseList).hasSize(databaseSizeBeforeUpdate);
        Desease testDesease = deseaseList.get(deseaseList.size() - 1);
        assertThat(testDesease.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testDesease.getDeseaseId()).isEqualTo(UPDATED_DESEASE_ID);
        assertThat(testDesease.getCaseInfo()).isEqualTo(UPDATED_CASE_INFO);
        assertThat(testDesease.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testDesease.getWeek()).isEqualTo(UPDATED_WEEK);
        assertThat(testDesease.getWeekEnding()).isEqualTo(UPDATED_WEEK_ENDING);
    }

    @Test
    @Transactional
    void putNonExistingDesease() throws Exception {
        int databaseSizeBeforeUpdate = deseaseRepository.findAll().size();
        desease.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeseaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, desease.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(desease))
            )
            .andExpect(status().isBadRequest());

        // Validate the Desease in the database
        List<Desease> deseaseList = deseaseRepository.findAll();
        assertThat(deseaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDesease() throws Exception {
        int databaseSizeBeforeUpdate = deseaseRepository.findAll().size();
        desease.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeseaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(desease))
            )
            .andExpect(status().isBadRequest());

        // Validate the Desease in the database
        List<Desease> deseaseList = deseaseRepository.findAll();
        assertThat(deseaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDesease() throws Exception {
        int databaseSizeBeforeUpdate = deseaseRepository.findAll().size();
        desease.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeseaseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(desease)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Desease in the database
        List<Desease> deseaseList = deseaseRepository.findAll();
        assertThat(deseaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeseaseWithPatch() throws Exception {
        // Initialize the database
        deseaseRepository.saveAndFlush(desease);

        int databaseSizeBeforeUpdate = deseaseRepository.findAll().size();

        // Update the desease using partial update
        Desease partialUpdatedDesease = new Desease();
        partialUpdatedDesease.setId(desease.getId());

        partialUpdatedDesease.deseaseId(UPDATED_DESEASE_ID).caseInfo(UPDATED_CASE_INFO).week(UPDATED_WEEK);

        restDeseaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDesease.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDesease))
            )
            .andExpect(status().isOk());

        // Validate the Desease in the database
        List<Desease> deseaseList = deseaseRepository.findAll();
        assertThat(deseaseList).hasSize(databaseSizeBeforeUpdate);
        Desease testDesease = deseaseList.get(deseaseList.size() - 1);
        assertThat(testDesease.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testDesease.getDeseaseId()).isEqualTo(UPDATED_DESEASE_ID);
        assertThat(testDesease.getCaseInfo()).isEqualTo(UPDATED_CASE_INFO);
        assertThat(testDesease.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testDesease.getWeek()).isEqualTo(UPDATED_WEEK);
        assertThat(testDesease.getWeekEnding()).isEqualTo(DEFAULT_WEEK_ENDING);
    }

    @Test
    @Transactional
    void fullUpdateDeseaseWithPatch() throws Exception {
        // Initialize the database
        deseaseRepository.saveAndFlush(desease);

        int databaseSizeBeforeUpdate = deseaseRepository.findAll().size();

        // Update the desease using partial update
        Desease partialUpdatedDesease = new Desease();
        partialUpdatedDesease.setId(desease.getId());

        partialUpdatedDesease
            .country(UPDATED_COUNTRY)
            .deseaseId(UPDATED_DESEASE_ID)
            .caseInfo(UPDATED_CASE_INFO)
            .year(UPDATED_YEAR)
            .week(UPDATED_WEEK)
            .weekEnding(UPDATED_WEEK_ENDING);

        restDeseaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDesease.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDesease))
            )
            .andExpect(status().isOk());

        // Validate the Desease in the database
        List<Desease> deseaseList = deseaseRepository.findAll();
        assertThat(deseaseList).hasSize(databaseSizeBeforeUpdate);
        Desease testDesease = deseaseList.get(deseaseList.size() - 1);
        assertThat(testDesease.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testDesease.getDeseaseId()).isEqualTo(UPDATED_DESEASE_ID);
        assertThat(testDesease.getCaseInfo()).isEqualTo(UPDATED_CASE_INFO);
        assertThat(testDesease.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testDesease.getWeek()).isEqualTo(UPDATED_WEEK);
        assertThat(testDesease.getWeekEnding()).isEqualTo(UPDATED_WEEK_ENDING);
    }

    @Test
    @Transactional
    void patchNonExistingDesease() throws Exception {
        int databaseSizeBeforeUpdate = deseaseRepository.findAll().size();
        desease.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeseaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, desease.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(desease))
            )
            .andExpect(status().isBadRequest());

        // Validate the Desease in the database
        List<Desease> deseaseList = deseaseRepository.findAll();
        assertThat(deseaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDesease() throws Exception {
        int databaseSizeBeforeUpdate = deseaseRepository.findAll().size();
        desease.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeseaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(desease))
            )
            .andExpect(status().isBadRequest());

        // Validate the Desease in the database
        List<Desease> deseaseList = deseaseRepository.findAll();
        assertThat(deseaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDesease() throws Exception {
        int databaseSizeBeforeUpdate = deseaseRepository.findAll().size();
        desease.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeseaseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(desease)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Desease in the database
        List<Desease> deseaseList = deseaseRepository.findAll();
        assertThat(deseaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDesease() throws Exception {
        // Initialize the database
        deseaseRepository.saveAndFlush(desease);

        int databaseSizeBeforeDelete = deseaseRepository.findAll().size();

        // Delete the desease
        restDeseaseMockMvc
            .perform(delete(ENTITY_API_URL_ID, desease.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Desease> deseaseList = deseaseRepository.findAll();
        assertThat(deseaseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
