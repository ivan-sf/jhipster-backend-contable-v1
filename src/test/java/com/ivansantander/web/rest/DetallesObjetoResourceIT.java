package com.ivansantander.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ivansantander.IntegrationTest;
import com.ivansantander.domain.DetallesObjeto;
import com.ivansantander.repository.DetallesObjetoRepository;
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
 * Integration tests for the {@link DetallesObjetoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DetallesObjetoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Long DEFAULT_DESCRIPCION = 1L;
    private static final Long UPDATED_DESCRIPCION = 2L;

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/detalles-objetos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DetallesObjetoRepository detallesObjetoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetallesObjetoMockMvc;

    private DetallesObjeto detallesObjeto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetallesObjeto createEntity(EntityManager em) {
        DetallesObjeto detallesObjeto = new DetallesObjeto()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return detallesObjeto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetallesObjeto createUpdatedEntity(EntityManager em) {
        DetallesObjeto detallesObjeto = new DetallesObjeto()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);
        return detallesObjeto;
    }

    @BeforeEach
    public void initTest() {
        detallesObjeto = createEntity(em);
    }

    @Test
    @Transactional
    void createDetallesObjeto() throws Exception {
        int databaseSizeBeforeCreate = detallesObjetoRepository.findAll().size();
        // Create the DetallesObjeto
        restDetallesObjetoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detallesObjeto))
            )
            .andExpect(status().isCreated());

        // Validate the DetallesObjeto in the database
        List<DetallesObjeto> detallesObjetoList = detallesObjetoRepository.findAll();
        assertThat(detallesObjetoList).hasSize(databaseSizeBeforeCreate + 1);
        DetallesObjeto testDetallesObjeto = detallesObjetoList.get(detallesObjetoList.size() - 1);
        assertThat(testDetallesObjeto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testDetallesObjeto.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testDetallesObjeto.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createDetallesObjetoWithExistingId() throws Exception {
        // Create the DetallesObjeto with an existing ID
        detallesObjeto.setId(1L);

        int databaseSizeBeforeCreate = detallesObjetoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetallesObjetoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detallesObjeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetallesObjeto in the database
        List<DetallesObjeto> detallesObjetoList = detallesObjetoRepository.findAll();
        assertThat(detallesObjetoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDetallesObjetos() throws Exception {
        // Initialize the database
        detallesObjetoRepository.saveAndFlush(detallesObjeto);

        // Get all the detallesObjetoList
        restDetallesObjetoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detallesObjeto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.intValue())))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @Test
    @Transactional
    void getDetallesObjeto() throws Exception {
        // Initialize the database
        detallesObjetoRepository.saveAndFlush(detallesObjeto);

        // Get the detallesObjeto
        restDetallesObjetoMockMvc
            .perform(get(ENTITY_API_URL_ID, detallesObjeto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detallesObjeto.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.intValue()))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDetallesObjeto() throws Exception {
        // Get the detallesObjeto
        restDetallesObjetoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDetallesObjeto() throws Exception {
        // Initialize the database
        detallesObjetoRepository.saveAndFlush(detallesObjeto);

        int databaseSizeBeforeUpdate = detallesObjetoRepository.findAll().size();

        // Update the detallesObjeto
        DetallesObjeto updatedDetallesObjeto = detallesObjetoRepository.findById(detallesObjeto.getId()).get();
        // Disconnect from session so that the updates on updatedDetallesObjeto are not directly saved in db
        em.detach(updatedDetallesObjeto);
        updatedDetallesObjeto.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restDetallesObjetoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDetallesObjeto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDetallesObjeto))
            )
            .andExpect(status().isOk());

        // Validate the DetallesObjeto in the database
        List<DetallesObjeto> detallesObjetoList = detallesObjetoRepository.findAll();
        assertThat(detallesObjetoList).hasSize(databaseSizeBeforeUpdate);
        DetallesObjeto testDetallesObjeto = detallesObjetoList.get(detallesObjetoList.size() - 1);
        assertThat(testDetallesObjeto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testDetallesObjeto.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testDetallesObjeto.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingDetallesObjeto() throws Exception {
        int databaseSizeBeforeUpdate = detallesObjetoRepository.findAll().size();
        detallesObjeto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetallesObjetoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detallesObjeto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detallesObjeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetallesObjeto in the database
        List<DetallesObjeto> detallesObjetoList = detallesObjetoRepository.findAll();
        assertThat(detallesObjetoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDetallesObjeto() throws Exception {
        int databaseSizeBeforeUpdate = detallesObjetoRepository.findAll().size();
        detallesObjeto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetallesObjetoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detallesObjeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetallesObjeto in the database
        List<DetallesObjeto> detallesObjetoList = detallesObjetoRepository.findAll();
        assertThat(detallesObjetoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDetallesObjeto() throws Exception {
        int databaseSizeBeforeUpdate = detallesObjetoRepository.findAll().size();
        detallesObjeto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetallesObjetoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detallesObjeto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetallesObjeto in the database
        List<DetallesObjeto> detallesObjetoList = detallesObjetoRepository.findAll();
        assertThat(detallesObjetoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDetallesObjetoWithPatch() throws Exception {
        // Initialize the database
        detallesObjetoRepository.saveAndFlush(detallesObjeto);

        int databaseSizeBeforeUpdate = detallesObjetoRepository.findAll().size();

        // Update the detallesObjeto using partial update
        DetallesObjeto partialUpdatedDetallesObjeto = new DetallesObjeto();
        partialUpdatedDetallesObjeto.setId(detallesObjeto.getId());

        partialUpdatedDetallesObjeto.nombre(UPDATED_NOMBRE).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restDetallesObjetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetallesObjeto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetallesObjeto))
            )
            .andExpect(status().isOk());

        // Validate the DetallesObjeto in the database
        List<DetallesObjeto> detallesObjetoList = detallesObjetoRepository.findAll();
        assertThat(detallesObjetoList).hasSize(databaseSizeBeforeUpdate);
        DetallesObjeto testDetallesObjeto = detallesObjetoList.get(detallesObjetoList.size() - 1);
        assertThat(testDetallesObjeto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testDetallesObjeto.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testDetallesObjeto.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateDetallesObjetoWithPatch() throws Exception {
        // Initialize the database
        detallesObjetoRepository.saveAndFlush(detallesObjeto);

        int databaseSizeBeforeUpdate = detallesObjetoRepository.findAll().size();

        // Update the detallesObjeto using partial update
        DetallesObjeto partialUpdatedDetallesObjeto = new DetallesObjeto();
        partialUpdatedDetallesObjeto.setId(detallesObjeto.getId());

        partialUpdatedDetallesObjeto.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restDetallesObjetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetallesObjeto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetallesObjeto))
            )
            .andExpect(status().isOk());

        // Validate the DetallesObjeto in the database
        List<DetallesObjeto> detallesObjetoList = detallesObjetoRepository.findAll();
        assertThat(detallesObjetoList).hasSize(databaseSizeBeforeUpdate);
        DetallesObjeto testDetallesObjeto = detallesObjetoList.get(detallesObjetoList.size() - 1);
        assertThat(testDetallesObjeto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testDetallesObjeto.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testDetallesObjeto.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingDetallesObjeto() throws Exception {
        int databaseSizeBeforeUpdate = detallesObjetoRepository.findAll().size();
        detallesObjeto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetallesObjetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, detallesObjeto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detallesObjeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetallesObjeto in the database
        List<DetallesObjeto> detallesObjetoList = detallesObjetoRepository.findAll();
        assertThat(detallesObjetoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDetallesObjeto() throws Exception {
        int databaseSizeBeforeUpdate = detallesObjetoRepository.findAll().size();
        detallesObjeto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetallesObjetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detallesObjeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetallesObjeto in the database
        List<DetallesObjeto> detallesObjetoList = detallesObjetoRepository.findAll();
        assertThat(detallesObjetoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDetallesObjeto() throws Exception {
        int databaseSizeBeforeUpdate = detallesObjetoRepository.findAll().size();
        detallesObjeto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetallesObjetoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(detallesObjeto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetallesObjeto in the database
        List<DetallesObjeto> detallesObjetoList = detallesObjetoRepository.findAll();
        assertThat(detallesObjetoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDetallesObjeto() throws Exception {
        // Initialize the database
        detallesObjetoRepository.saveAndFlush(detallesObjeto);

        int databaseSizeBeforeDelete = detallesObjetoRepository.findAll().size();

        // Delete the detallesObjeto
        restDetallesObjetoMockMvc
            .perform(delete(ENTITY_API_URL_ID, detallesObjeto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DetallesObjeto> detallesObjetoList = detallesObjetoRepository.findAll();
        assertThat(detallesObjetoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
