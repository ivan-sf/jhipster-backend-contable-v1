package com.ivansantander.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ivansantander.IntegrationTest;
import com.ivansantander.domain.Paquete;
import com.ivansantander.repository.PaqueteRepository;
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
 * Integration tests for the {@link PaqueteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaqueteResourceIT {

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final String ENTITY_API_URL = "/api/paquetes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaqueteRepository paqueteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaqueteMockMvc;

    private Paquete paquete;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paquete createEntity(EntityManager em) {
        Paquete paquete = new Paquete().cantidad(DEFAULT_CANTIDAD);
        return paquete;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paquete createUpdatedEntity(EntityManager em) {
        Paquete paquete = new Paquete().cantidad(UPDATED_CANTIDAD);
        return paquete;
    }

    @BeforeEach
    public void initTest() {
        paquete = createEntity(em);
    }

    @Test
    @Transactional
    void createPaquete() throws Exception {
        int databaseSizeBeforeCreate = paqueteRepository.findAll().size();
        // Create the Paquete
        restPaqueteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paquete)))
            .andExpect(status().isCreated());

        // Validate the Paquete in the database
        List<Paquete> paqueteList = paqueteRepository.findAll();
        assertThat(paqueteList).hasSize(databaseSizeBeforeCreate + 1);
        Paquete testPaquete = paqueteList.get(paqueteList.size() - 1);
        assertThat(testPaquete.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
    }

    @Test
    @Transactional
    void createPaqueteWithExistingId() throws Exception {
        // Create the Paquete with an existing ID
        paquete.setId(1L);

        int databaseSizeBeforeCreate = paqueteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaqueteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paquete)))
            .andExpect(status().isBadRequest());

        // Validate the Paquete in the database
        List<Paquete> paqueteList = paqueteRepository.findAll();
        assertThat(paqueteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPaquetes() throws Exception {
        // Initialize the database
        paqueteRepository.saveAndFlush(paquete);

        // Get all the paqueteList
        restPaqueteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paquete.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)));
    }

    @Test
    @Transactional
    void getPaquete() throws Exception {
        // Initialize the database
        paqueteRepository.saveAndFlush(paquete);

        // Get the paquete
        restPaqueteMockMvc
            .perform(get(ENTITY_API_URL_ID, paquete.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paquete.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD));
    }

    @Test
    @Transactional
    void getNonExistingPaquete() throws Exception {
        // Get the paquete
        restPaqueteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPaquete() throws Exception {
        // Initialize the database
        paqueteRepository.saveAndFlush(paquete);

        int databaseSizeBeforeUpdate = paqueteRepository.findAll().size();

        // Update the paquete
        Paquete updatedPaquete = paqueteRepository.findById(paquete.getId()).get();
        // Disconnect from session so that the updates on updatedPaquete are not directly saved in db
        em.detach(updatedPaquete);
        updatedPaquete.cantidad(UPDATED_CANTIDAD);

        restPaqueteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaquete.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPaquete))
            )
            .andExpect(status().isOk());

        // Validate the Paquete in the database
        List<Paquete> paqueteList = paqueteRepository.findAll();
        assertThat(paqueteList).hasSize(databaseSizeBeforeUpdate);
        Paquete testPaquete = paqueteList.get(paqueteList.size() - 1);
        assertThat(testPaquete.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void putNonExistingPaquete() throws Exception {
        int databaseSizeBeforeUpdate = paqueteRepository.findAll().size();
        paquete.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaqueteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paquete.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paquete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paquete in the database
        List<Paquete> paqueteList = paqueteRepository.findAll();
        assertThat(paqueteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaquete() throws Exception {
        int databaseSizeBeforeUpdate = paqueteRepository.findAll().size();
        paquete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaqueteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paquete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paquete in the database
        List<Paquete> paqueteList = paqueteRepository.findAll();
        assertThat(paqueteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaquete() throws Exception {
        int databaseSizeBeforeUpdate = paqueteRepository.findAll().size();
        paquete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaqueteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paquete)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paquete in the database
        List<Paquete> paqueteList = paqueteRepository.findAll();
        assertThat(paqueteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaqueteWithPatch() throws Exception {
        // Initialize the database
        paqueteRepository.saveAndFlush(paquete);

        int databaseSizeBeforeUpdate = paqueteRepository.findAll().size();

        // Update the paquete using partial update
        Paquete partialUpdatedPaquete = new Paquete();
        partialUpdatedPaquete.setId(paquete.getId());

        partialUpdatedPaquete.cantidad(UPDATED_CANTIDAD);

        restPaqueteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaquete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaquete))
            )
            .andExpect(status().isOk());

        // Validate the Paquete in the database
        List<Paquete> paqueteList = paqueteRepository.findAll();
        assertThat(paqueteList).hasSize(databaseSizeBeforeUpdate);
        Paquete testPaquete = paqueteList.get(paqueteList.size() - 1);
        assertThat(testPaquete.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void fullUpdatePaqueteWithPatch() throws Exception {
        // Initialize the database
        paqueteRepository.saveAndFlush(paquete);

        int databaseSizeBeforeUpdate = paqueteRepository.findAll().size();

        // Update the paquete using partial update
        Paquete partialUpdatedPaquete = new Paquete();
        partialUpdatedPaquete.setId(paquete.getId());

        partialUpdatedPaquete.cantidad(UPDATED_CANTIDAD);

        restPaqueteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaquete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaquete))
            )
            .andExpect(status().isOk());

        // Validate the Paquete in the database
        List<Paquete> paqueteList = paqueteRepository.findAll();
        assertThat(paqueteList).hasSize(databaseSizeBeforeUpdate);
        Paquete testPaquete = paqueteList.get(paqueteList.size() - 1);
        assertThat(testPaquete.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void patchNonExistingPaquete() throws Exception {
        int databaseSizeBeforeUpdate = paqueteRepository.findAll().size();
        paquete.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaqueteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paquete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paquete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paquete in the database
        List<Paquete> paqueteList = paqueteRepository.findAll();
        assertThat(paqueteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaquete() throws Exception {
        int databaseSizeBeforeUpdate = paqueteRepository.findAll().size();
        paquete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaqueteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paquete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paquete in the database
        List<Paquete> paqueteList = paqueteRepository.findAll();
        assertThat(paqueteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaquete() throws Exception {
        int databaseSizeBeforeUpdate = paqueteRepository.findAll().size();
        paquete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaqueteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(paquete)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paquete in the database
        List<Paquete> paqueteList = paqueteRepository.findAll();
        assertThat(paqueteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaquete() throws Exception {
        // Initialize the database
        paqueteRepository.saveAndFlush(paquete);

        int databaseSizeBeforeDelete = paqueteRepository.findAll().size();

        // Delete the paquete
        restPaqueteMockMvc
            .perform(delete(ENTITY_API_URL_ID, paquete.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Paquete> paqueteList = paqueteRepository.findAll();
        assertThat(paqueteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
