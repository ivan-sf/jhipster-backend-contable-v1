package com.ivansantander.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ivansantander.IntegrationTest;
import com.ivansantander.domain.TipoFactura;
import com.ivansantander.repository.TipoFacturaRepository;
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
 * Integration tests for the {@link TipoFacturaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoFacturaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PREFIJO_INICIAL = 1;
    private static final Integer UPDATED_PREFIJO_INICIAL = 2;

    private static final Integer DEFAULT_PREFIJO_FINAL = 1;
    private static final Integer UPDATED_PREFIJO_FINAL = 2;

    private static final Integer DEFAULT_PREFIJO_ACTUAL = 1;
    private static final Integer UPDATED_PREFIJO_ACTUAL = 2;

    private static final String ENTITY_API_URL = "/api/tipo-facturas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoFacturaRepository tipoFacturaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoFacturaMockMvc;

    private TipoFactura tipoFactura;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoFactura createEntity(EntityManager em) {
        TipoFactura tipoFactura = new TipoFactura()
            .nombre(DEFAULT_NOMBRE)
            .prefijoInicial(DEFAULT_PREFIJO_INICIAL)
            .prefijoFinal(DEFAULT_PREFIJO_FINAL)
            .prefijoActual(DEFAULT_PREFIJO_ACTUAL);
        return tipoFactura;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoFactura createUpdatedEntity(EntityManager em) {
        TipoFactura tipoFactura = new TipoFactura()
            .nombre(UPDATED_NOMBRE)
            .prefijoInicial(UPDATED_PREFIJO_INICIAL)
            .prefijoFinal(UPDATED_PREFIJO_FINAL)
            .prefijoActual(UPDATED_PREFIJO_ACTUAL);
        return tipoFactura;
    }

    @BeforeEach
    public void initTest() {
        tipoFactura = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoFactura() throws Exception {
        int databaseSizeBeforeCreate = tipoFacturaRepository.findAll().size();
        // Create the TipoFactura
        restTipoFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoFactura)))
            .andExpect(status().isCreated());

        // Validate the TipoFactura in the database
        List<TipoFactura> tipoFacturaList = tipoFacturaRepository.findAll();
        assertThat(tipoFacturaList).hasSize(databaseSizeBeforeCreate + 1);
        TipoFactura testTipoFactura = tipoFacturaList.get(tipoFacturaList.size() - 1);
        assertThat(testTipoFactura.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTipoFactura.getPrefijoInicial()).isEqualTo(DEFAULT_PREFIJO_INICIAL);
        assertThat(testTipoFactura.getPrefijoFinal()).isEqualTo(DEFAULT_PREFIJO_FINAL);
        assertThat(testTipoFactura.getPrefijoActual()).isEqualTo(DEFAULT_PREFIJO_ACTUAL);
    }

    @Test
    @Transactional
    void createTipoFacturaWithExistingId() throws Exception {
        // Create the TipoFactura with an existing ID
        tipoFactura.setId(1L);

        int databaseSizeBeforeCreate = tipoFacturaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoFactura)))
            .andExpect(status().isBadRequest());

        // Validate the TipoFactura in the database
        List<TipoFactura> tipoFacturaList = tipoFacturaRepository.findAll();
        assertThat(tipoFacturaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTipoFacturas() throws Exception {
        // Initialize the database
        tipoFacturaRepository.saveAndFlush(tipoFactura);

        // Get all the tipoFacturaList
        restTipoFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoFactura.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].prefijoInicial").value(hasItem(DEFAULT_PREFIJO_INICIAL)))
            .andExpect(jsonPath("$.[*].prefijoFinal").value(hasItem(DEFAULT_PREFIJO_FINAL)))
            .andExpect(jsonPath("$.[*].prefijoActual").value(hasItem(DEFAULT_PREFIJO_ACTUAL)));
    }

    @Test
    @Transactional
    void getTipoFactura() throws Exception {
        // Initialize the database
        tipoFacturaRepository.saveAndFlush(tipoFactura);

        // Get the tipoFactura
        restTipoFacturaMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoFactura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoFactura.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.prefijoInicial").value(DEFAULT_PREFIJO_INICIAL))
            .andExpect(jsonPath("$.prefijoFinal").value(DEFAULT_PREFIJO_FINAL))
            .andExpect(jsonPath("$.prefijoActual").value(DEFAULT_PREFIJO_ACTUAL));
    }

    @Test
    @Transactional
    void getNonExistingTipoFactura() throws Exception {
        // Get the tipoFactura
        restTipoFacturaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTipoFactura() throws Exception {
        // Initialize the database
        tipoFacturaRepository.saveAndFlush(tipoFactura);

        int databaseSizeBeforeUpdate = tipoFacturaRepository.findAll().size();

        // Update the tipoFactura
        TipoFactura updatedTipoFactura = tipoFacturaRepository.findById(tipoFactura.getId()).get();
        // Disconnect from session so that the updates on updatedTipoFactura are not directly saved in db
        em.detach(updatedTipoFactura);
        updatedTipoFactura
            .nombre(UPDATED_NOMBRE)
            .prefijoInicial(UPDATED_PREFIJO_INICIAL)
            .prefijoFinal(UPDATED_PREFIJO_FINAL)
            .prefijoActual(UPDATED_PREFIJO_ACTUAL);

        restTipoFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTipoFactura.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTipoFactura))
            )
            .andExpect(status().isOk());

        // Validate the TipoFactura in the database
        List<TipoFactura> tipoFacturaList = tipoFacturaRepository.findAll();
        assertThat(tipoFacturaList).hasSize(databaseSizeBeforeUpdate);
        TipoFactura testTipoFactura = tipoFacturaList.get(tipoFacturaList.size() - 1);
        assertThat(testTipoFactura.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTipoFactura.getPrefijoInicial()).isEqualTo(UPDATED_PREFIJO_INICIAL);
        assertThat(testTipoFactura.getPrefijoFinal()).isEqualTo(UPDATED_PREFIJO_FINAL);
        assertThat(testTipoFactura.getPrefijoActual()).isEqualTo(UPDATED_PREFIJO_ACTUAL);
    }

    @Test
    @Transactional
    void putNonExistingTipoFactura() throws Exception {
        int databaseSizeBeforeUpdate = tipoFacturaRepository.findAll().size();
        tipoFactura.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoFactura.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoFactura))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoFactura in the database
        List<TipoFactura> tipoFacturaList = tipoFacturaRepository.findAll();
        assertThat(tipoFacturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoFactura() throws Exception {
        int databaseSizeBeforeUpdate = tipoFacturaRepository.findAll().size();
        tipoFactura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoFactura))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoFactura in the database
        List<TipoFactura> tipoFacturaList = tipoFacturaRepository.findAll();
        assertThat(tipoFacturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoFactura() throws Exception {
        int databaseSizeBeforeUpdate = tipoFacturaRepository.findAll().size();
        tipoFactura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoFacturaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoFactura)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoFactura in the database
        List<TipoFactura> tipoFacturaList = tipoFacturaRepository.findAll();
        assertThat(tipoFacturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoFacturaWithPatch() throws Exception {
        // Initialize the database
        tipoFacturaRepository.saveAndFlush(tipoFactura);

        int databaseSizeBeforeUpdate = tipoFacturaRepository.findAll().size();

        // Update the tipoFactura using partial update
        TipoFactura partialUpdatedTipoFactura = new TipoFactura();
        partialUpdatedTipoFactura.setId(tipoFactura.getId());

        partialUpdatedTipoFactura.prefijoActual(UPDATED_PREFIJO_ACTUAL);

        restTipoFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoFactura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoFactura))
            )
            .andExpect(status().isOk());

        // Validate the TipoFactura in the database
        List<TipoFactura> tipoFacturaList = tipoFacturaRepository.findAll();
        assertThat(tipoFacturaList).hasSize(databaseSizeBeforeUpdate);
        TipoFactura testTipoFactura = tipoFacturaList.get(tipoFacturaList.size() - 1);
        assertThat(testTipoFactura.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTipoFactura.getPrefijoInicial()).isEqualTo(DEFAULT_PREFIJO_INICIAL);
        assertThat(testTipoFactura.getPrefijoFinal()).isEqualTo(DEFAULT_PREFIJO_FINAL);
        assertThat(testTipoFactura.getPrefijoActual()).isEqualTo(UPDATED_PREFIJO_ACTUAL);
    }

    @Test
    @Transactional
    void fullUpdateTipoFacturaWithPatch() throws Exception {
        // Initialize the database
        tipoFacturaRepository.saveAndFlush(tipoFactura);

        int databaseSizeBeforeUpdate = tipoFacturaRepository.findAll().size();

        // Update the tipoFactura using partial update
        TipoFactura partialUpdatedTipoFactura = new TipoFactura();
        partialUpdatedTipoFactura.setId(tipoFactura.getId());

        partialUpdatedTipoFactura
            .nombre(UPDATED_NOMBRE)
            .prefijoInicial(UPDATED_PREFIJO_INICIAL)
            .prefijoFinal(UPDATED_PREFIJO_FINAL)
            .prefijoActual(UPDATED_PREFIJO_ACTUAL);

        restTipoFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoFactura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoFactura))
            )
            .andExpect(status().isOk());

        // Validate the TipoFactura in the database
        List<TipoFactura> tipoFacturaList = tipoFacturaRepository.findAll();
        assertThat(tipoFacturaList).hasSize(databaseSizeBeforeUpdate);
        TipoFactura testTipoFactura = tipoFacturaList.get(tipoFacturaList.size() - 1);
        assertThat(testTipoFactura.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTipoFactura.getPrefijoInicial()).isEqualTo(UPDATED_PREFIJO_INICIAL);
        assertThat(testTipoFactura.getPrefijoFinal()).isEqualTo(UPDATED_PREFIJO_FINAL);
        assertThat(testTipoFactura.getPrefijoActual()).isEqualTo(UPDATED_PREFIJO_ACTUAL);
    }

    @Test
    @Transactional
    void patchNonExistingTipoFactura() throws Exception {
        int databaseSizeBeforeUpdate = tipoFacturaRepository.findAll().size();
        tipoFactura.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoFactura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoFactura))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoFactura in the database
        List<TipoFactura> tipoFacturaList = tipoFacturaRepository.findAll();
        assertThat(tipoFacturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoFactura() throws Exception {
        int databaseSizeBeforeUpdate = tipoFacturaRepository.findAll().size();
        tipoFactura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoFactura))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoFactura in the database
        List<TipoFactura> tipoFacturaList = tipoFacturaRepository.findAll();
        assertThat(tipoFacturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoFactura() throws Exception {
        int databaseSizeBeforeUpdate = tipoFacturaRepository.findAll().size();
        tipoFactura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tipoFactura))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoFactura in the database
        List<TipoFactura> tipoFacturaList = tipoFacturaRepository.findAll();
        assertThat(tipoFacturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoFactura() throws Exception {
        // Initialize the database
        tipoFacturaRepository.saveAndFlush(tipoFactura);

        int databaseSizeBeforeDelete = tipoFacturaRepository.findAll().size();

        // Delete the tipoFactura
        restTipoFacturaMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoFactura.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoFactura> tipoFacturaList = tipoFacturaRepository.findAll();
        assertThat(tipoFacturaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
