package com.ivansantander.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ivansantander.IntegrationTest;
import com.ivansantander.domain.Vencimiento;
import com.ivansantander.repository.VencimientoRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link VencimientoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VencimientoResourceIT {

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/vencimientos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VencimientoRepository vencimientoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVencimientoMockMvc;

    private Vencimiento vencimiento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vencimiento createEntity(EntityManager em) {
        Vencimiento vencimiento = new Vencimiento().fecha(DEFAULT_FECHA);
        return vencimiento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vencimiento createUpdatedEntity(EntityManager em) {
        Vencimiento vencimiento = new Vencimiento().fecha(UPDATED_FECHA);
        return vencimiento;
    }

    @BeforeEach
    public void initTest() {
        vencimiento = createEntity(em);
    }

    @Test
    @Transactional
    void createVencimiento() throws Exception {
        int databaseSizeBeforeCreate = vencimientoRepository.findAll().size();
        // Create the Vencimiento
        restVencimientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vencimiento)))
            .andExpect(status().isCreated());

        // Validate the Vencimiento in the database
        List<Vencimiento> vencimientoList = vencimientoRepository.findAll();
        assertThat(vencimientoList).hasSize(databaseSizeBeforeCreate + 1);
        Vencimiento testVencimiento = vencimientoList.get(vencimientoList.size() - 1);
        assertThat(testVencimiento.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void createVencimientoWithExistingId() throws Exception {
        // Create the Vencimiento with an existing ID
        vencimiento.setId(1L);

        int databaseSizeBeforeCreate = vencimientoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVencimientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vencimiento)))
            .andExpect(status().isBadRequest());

        // Validate the Vencimiento in the database
        List<Vencimiento> vencimientoList = vencimientoRepository.findAll();
        assertThat(vencimientoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVencimientos() throws Exception {
        // Initialize the database
        vencimientoRepository.saveAndFlush(vencimiento);

        // Get all the vencimientoList
        restVencimientoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vencimiento.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));
    }

    @Test
    @Transactional
    void getVencimiento() throws Exception {
        // Initialize the database
        vencimientoRepository.saveAndFlush(vencimiento);

        // Get the vencimiento
        restVencimientoMockMvc
            .perform(get(ENTITY_API_URL_ID, vencimiento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vencimiento.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVencimiento() throws Exception {
        // Get the vencimiento
        restVencimientoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVencimiento() throws Exception {
        // Initialize the database
        vencimientoRepository.saveAndFlush(vencimiento);

        int databaseSizeBeforeUpdate = vencimientoRepository.findAll().size();

        // Update the vencimiento
        Vencimiento updatedVencimiento = vencimientoRepository.findById(vencimiento.getId()).get();
        // Disconnect from session so that the updates on updatedVencimiento are not directly saved in db
        em.detach(updatedVencimiento);
        updatedVencimiento.fecha(UPDATED_FECHA);

        restVencimientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVencimiento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVencimiento))
            )
            .andExpect(status().isOk());

        // Validate the Vencimiento in the database
        List<Vencimiento> vencimientoList = vencimientoRepository.findAll();
        assertThat(vencimientoList).hasSize(databaseSizeBeforeUpdate);
        Vencimiento testVencimiento = vencimientoList.get(vencimientoList.size() - 1);
        assertThat(testVencimiento.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void putNonExistingVencimiento() throws Exception {
        int databaseSizeBeforeUpdate = vencimientoRepository.findAll().size();
        vencimiento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVencimientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vencimiento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vencimiento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vencimiento in the database
        List<Vencimiento> vencimientoList = vencimientoRepository.findAll();
        assertThat(vencimientoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVencimiento() throws Exception {
        int databaseSizeBeforeUpdate = vencimientoRepository.findAll().size();
        vencimiento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVencimientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vencimiento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vencimiento in the database
        List<Vencimiento> vencimientoList = vencimientoRepository.findAll();
        assertThat(vencimientoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVencimiento() throws Exception {
        int databaseSizeBeforeUpdate = vencimientoRepository.findAll().size();
        vencimiento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVencimientoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vencimiento)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vencimiento in the database
        List<Vencimiento> vencimientoList = vencimientoRepository.findAll();
        assertThat(vencimientoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVencimientoWithPatch() throws Exception {
        // Initialize the database
        vencimientoRepository.saveAndFlush(vencimiento);

        int databaseSizeBeforeUpdate = vencimientoRepository.findAll().size();

        // Update the vencimiento using partial update
        Vencimiento partialUpdatedVencimiento = new Vencimiento();
        partialUpdatedVencimiento.setId(vencimiento.getId());

        partialUpdatedVencimiento.fecha(UPDATED_FECHA);

        restVencimientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVencimiento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVencimiento))
            )
            .andExpect(status().isOk());

        // Validate the Vencimiento in the database
        List<Vencimiento> vencimientoList = vencimientoRepository.findAll();
        assertThat(vencimientoList).hasSize(databaseSizeBeforeUpdate);
        Vencimiento testVencimiento = vencimientoList.get(vencimientoList.size() - 1);
        assertThat(testVencimiento.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void fullUpdateVencimientoWithPatch() throws Exception {
        // Initialize the database
        vencimientoRepository.saveAndFlush(vencimiento);

        int databaseSizeBeforeUpdate = vencimientoRepository.findAll().size();

        // Update the vencimiento using partial update
        Vencimiento partialUpdatedVencimiento = new Vencimiento();
        partialUpdatedVencimiento.setId(vencimiento.getId());

        partialUpdatedVencimiento.fecha(UPDATED_FECHA);

        restVencimientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVencimiento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVencimiento))
            )
            .andExpect(status().isOk());

        // Validate the Vencimiento in the database
        List<Vencimiento> vencimientoList = vencimientoRepository.findAll();
        assertThat(vencimientoList).hasSize(databaseSizeBeforeUpdate);
        Vencimiento testVencimiento = vencimientoList.get(vencimientoList.size() - 1);
        assertThat(testVencimiento.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void patchNonExistingVencimiento() throws Exception {
        int databaseSizeBeforeUpdate = vencimientoRepository.findAll().size();
        vencimiento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVencimientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vencimiento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vencimiento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vencimiento in the database
        List<Vencimiento> vencimientoList = vencimientoRepository.findAll();
        assertThat(vencimientoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVencimiento() throws Exception {
        int databaseSizeBeforeUpdate = vencimientoRepository.findAll().size();
        vencimiento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVencimientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vencimiento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vencimiento in the database
        List<Vencimiento> vencimientoList = vencimientoRepository.findAll();
        assertThat(vencimientoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVencimiento() throws Exception {
        int databaseSizeBeforeUpdate = vencimientoRepository.findAll().size();
        vencimiento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVencimientoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vencimiento))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vencimiento in the database
        List<Vencimiento> vencimientoList = vencimientoRepository.findAll();
        assertThat(vencimientoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVencimiento() throws Exception {
        // Initialize the database
        vencimientoRepository.saveAndFlush(vencimiento);

        int databaseSizeBeforeDelete = vencimientoRepository.findAll().size();

        // Delete the vencimiento
        restVencimientoMockMvc
            .perform(delete(ENTITY_API_URL_ID, vencimiento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vencimiento> vencimientoList = vencimientoRepository.findAll();
        assertThat(vencimientoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
