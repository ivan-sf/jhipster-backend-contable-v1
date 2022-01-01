package com.ivansantander.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ivansantander.IntegrationTest;
import com.ivansantander.domain.TipoObjeto;
import com.ivansantander.repository.TipoObjetoRepository;
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
 * Integration tests for the {@link TipoObjetoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoObjetoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_FECHA_REGISTRO = "AAAAAAAAAA";
    private static final String UPDATED_FECHA_REGISTRO = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_DIAN = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_DIAN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-objetos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoObjetoRepository tipoObjetoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoObjetoMockMvc;

    private TipoObjeto tipoObjeto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoObjeto createEntity(EntityManager em) {
        TipoObjeto tipoObjeto = new TipoObjeto()
            .nombre(DEFAULT_NOMBRE)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO)
            .codigoDian(DEFAULT_CODIGO_DIAN);
        return tipoObjeto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoObjeto createUpdatedEntity(EntityManager em) {
        TipoObjeto tipoObjeto = new TipoObjeto()
            .nombre(UPDATED_NOMBRE)
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .codigoDian(UPDATED_CODIGO_DIAN);
        return tipoObjeto;
    }

    @BeforeEach
    public void initTest() {
        tipoObjeto = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoObjeto() throws Exception {
        int databaseSizeBeforeCreate = tipoObjetoRepository.findAll().size();
        // Create the TipoObjeto
        restTipoObjetoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoObjeto)))
            .andExpect(status().isCreated());

        // Validate the TipoObjeto in the database
        List<TipoObjeto> tipoObjetoList = tipoObjetoRepository.findAll();
        assertThat(tipoObjetoList).hasSize(databaseSizeBeforeCreate + 1);
        TipoObjeto testTipoObjeto = tipoObjetoList.get(tipoObjetoList.size() - 1);
        assertThat(testTipoObjeto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTipoObjeto.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
        assertThat(testTipoObjeto.getCodigoDian()).isEqualTo(DEFAULT_CODIGO_DIAN);
    }

    @Test
    @Transactional
    void createTipoObjetoWithExistingId() throws Exception {
        // Create the TipoObjeto with an existing ID
        tipoObjeto.setId(1L);

        int databaseSizeBeforeCreate = tipoObjetoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoObjetoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoObjeto)))
            .andExpect(status().isBadRequest());

        // Validate the TipoObjeto in the database
        List<TipoObjeto> tipoObjetoList = tipoObjetoRepository.findAll();
        assertThat(tipoObjetoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTipoObjetos() throws Exception {
        // Initialize the database
        tipoObjetoRepository.saveAndFlush(tipoObjeto);

        // Get all the tipoObjetoList
        restTipoObjetoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoObjeto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO)))
            .andExpect(jsonPath("$.[*].codigoDian").value(hasItem(DEFAULT_CODIGO_DIAN)));
    }

    @Test
    @Transactional
    void getTipoObjeto() throws Exception {
        // Initialize the database
        tipoObjetoRepository.saveAndFlush(tipoObjeto);

        // Get the tipoObjeto
        restTipoObjetoMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoObjeto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoObjeto.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO))
            .andExpect(jsonPath("$.codigoDian").value(DEFAULT_CODIGO_DIAN));
    }

    @Test
    @Transactional
    void getNonExistingTipoObjeto() throws Exception {
        // Get the tipoObjeto
        restTipoObjetoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTipoObjeto() throws Exception {
        // Initialize the database
        tipoObjetoRepository.saveAndFlush(tipoObjeto);

        int databaseSizeBeforeUpdate = tipoObjetoRepository.findAll().size();

        // Update the tipoObjeto
        TipoObjeto updatedTipoObjeto = tipoObjetoRepository.findById(tipoObjeto.getId()).get();
        // Disconnect from session so that the updates on updatedTipoObjeto are not directly saved in db
        em.detach(updatedTipoObjeto);
        updatedTipoObjeto.nombre(UPDATED_NOMBRE).fechaRegistro(UPDATED_FECHA_REGISTRO).codigoDian(UPDATED_CODIGO_DIAN);

        restTipoObjetoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTipoObjeto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTipoObjeto))
            )
            .andExpect(status().isOk());

        // Validate the TipoObjeto in the database
        List<TipoObjeto> tipoObjetoList = tipoObjetoRepository.findAll();
        assertThat(tipoObjetoList).hasSize(databaseSizeBeforeUpdate);
        TipoObjeto testTipoObjeto = tipoObjetoList.get(tipoObjetoList.size() - 1);
        assertThat(testTipoObjeto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTipoObjeto.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
        assertThat(testTipoObjeto.getCodigoDian()).isEqualTo(UPDATED_CODIGO_DIAN);
    }

    @Test
    @Transactional
    void putNonExistingTipoObjeto() throws Exception {
        int databaseSizeBeforeUpdate = tipoObjetoRepository.findAll().size();
        tipoObjeto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoObjetoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoObjeto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoObjeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoObjeto in the database
        List<TipoObjeto> tipoObjetoList = tipoObjetoRepository.findAll();
        assertThat(tipoObjetoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoObjeto() throws Exception {
        int databaseSizeBeforeUpdate = tipoObjetoRepository.findAll().size();
        tipoObjeto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoObjetoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoObjeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoObjeto in the database
        List<TipoObjeto> tipoObjetoList = tipoObjetoRepository.findAll();
        assertThat(tipoObjetoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoObjeto() throws Exception {
        int databaseSizeBeforeUpdate = tipoObjetoRepository.findAll().size();
        tipoObjeto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoObjetoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoObjeto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoObjeto in the database
        List<TipoObjeto> tipoObjetoList = tipoObjetoRepository.findAll();
        assertThat(tipoObjetoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoObjetoWithPatch() throws Exception {
        // Initialize the database
        tipoObjetoRepository.saveAndFlush(tipoObjeto);

        int databaseSizeBeforeUpdate = tipoObjetoRepository.findAll().size();

        // Update the tipoObjeto using partial update
        TipoObjeto partialUpdatedTipoObjeto = new TipoObjeto();
        partialUpdatedTipoObjeto.setId(tipoObjeto.getId());

        partialUpdatedTipoObjeto.codigoDian(UPDATED_CODIGO_DIAN);

        restTipoObjetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoObjeto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoObjeto))
            )
            .andExpect(status().isOk());

        // Validate the TipoObjeto in the database
        List<TipoObjeto> tipoObjetoList = tipoObjetoRepository.findAll();
        assertThat(tipoObjetoList).hasSize(databaseSizeBeforeUpdate);
        TipoObjeto testTipoObjeto = tipoObjetoList.get(tipoObjetoList.size() - 1);
        assertThat(testTipoObjeto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTipoObjeto.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
        assertThat(testTipoObjeto.getCodigoDian()).isEqualTo(UPDATED_CODIGO_DIAN);
    }

    @Test
    @Transactional
    void fullUpdateTipoObjetoWithPatch() throws Exception {
        // Initialize the database
        tipoObjetoRepository.saveAndFlush(tipoObjeto);

        int databaseSizeBeforeUpdate = tipoObjetoRepository.findAll().size();

        // Update the tipoObjeto using partial update
        TipoObjeto partialUpdatedTipoObjeto = new TipoObjeto();
        partialUpdatedTipoObjeto.setId(tipoObjeto.getId());

        partialUpdatedTipoObjeto.nombre(UPDATED_NOMBRE).fechaRegistro(UPDATED_FECHA_REGISTRO).codigoDian(UPDATED_CODIGO_DIAN);

        restTipoObjetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoObjeto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoObjeto))
            )
            .andExpect(status().isOk());

        // Validate the TipoObjeto in the database
        List<TipoObjeto> tipoObjetoList = tipoObjetoRepository.findAll();
        assertThat(tipoObjetoList).hasSize(databaseSizeBeforeUpdate);
        TipoObjeto testTipoObjeto = tipoObjetoList.get(tipoObjetoList.size() - 1);
        assertThat(testTipoObjeto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTipoObjeto.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
        assertThat(testTipoObjeto.getCodigoDian()).isEqualTo(UPDATED_CODIGO_DIAN);
    }

    @Test
    @Transactional
    void patchNonExistingTipoObjeto() throws Exception {
        int databaseSizeBeforeUpdate = tipoObjetoRepository.findAll().size();
        tipoObjeto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoObjetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoObjeto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoObjeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoObjeto in the database
        List<TipoObjeto> tipoObjetoList = tipoObjetoRepository.findAll();
        assertThat(tipoObjetoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoObjeto() throws Exception {
        int databaseSizeBeforeUpdate = tipoObjetoRepository.findAll().size();
        tipoObjeto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoObjetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoObjeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoObjeto in the database
        List<TipoObjeto> tipoObjetoList = tipoObjetoRepository.findAll();
        assertThat(tipoObjetoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoObjeto() throws Exception {
        int databaseSizeBeforeUpdate = tipoObjetoRepository.findAll().size();
        tipoObjeto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoObjetoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tipoObjeto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoObjeto in the database
        List<TipoObjeto> tipoObjetoList = tipoObjetoRepository.findAll();
        assertThat(tipoObjetoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoObjeto() throws Exception {
        // Initialize the database
        tipoObjetoRepository.saveAndFlush(tipoObjeto);

        int databaseSizeBeforeDelete = tipoObjetoRepository.findAll().size();

        // Delete the tipoObjeto
        restTipoObjetoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoObjeto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoObjeto> tipoObjetoList = tipoObjetoRepository.findAll();
        assertThat(tipoObjetoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
