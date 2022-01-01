package com.ivansantander.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ivansantander.IntegrationTest;
import com.ivansantander.domain.Trabajo;
import com.ivansantander.repository.TrabajoRepository;
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
 * Integration tests for the {@link TrabajoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrabajoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CARGO = "AAAAAAAAAA";
    private static final String UPDATED_CARGO = "BBBBBBBBBB";

    private static final Integer DEFAULT_SALARIO = 1;
    private static final Integer UPDATED_SALARIO = 2;

    private static final String DEFAULT_SALUD = "AAAAAAAAAA";
    private static final String UPDATED_SALUD = "BBBBBBBBBB";

    private static final String DEFAULT_PENSION = "AAAAAAAAAA";
    private static final String UPDATED_PENSION = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACON = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACON = "BBBBBBBBBB";

    private static final String DEFAULT_FECHA_REGISTRO = "AAAAAAAAAA";
    private static final String UPDATED_FECHA_REGISTRO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/trabajos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrabajoRepository trabajoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrabajoMockMvc;

    private Trabajo trabajo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trabajo createEntity(EntityManager em) {
        Trabajo trabajo = new Trabajo()
            .nombre(DEFAULT_NOMBRE)
            .cargo(DEFAULT_CARGO)
            .salario(DEFAULT_SALARIO)
            .salud(DEFAULT_SALUD)
            .pension(DEFAULT_PENSION)
            .observacon(DEFAULT_OBSERVACON)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return trabajo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trabajo createUpdatedEntity(EntityManager em) {
        Trabajo trabajo = new Trabajo()
            .nombre(UPDATED_NOMBRE)
            .cargo(UPDATED_CARGO)
            .salario(UPDATED_SALARIO)
            .salud(UPDATED_SALUD)
            .pension(UPDATED_PENSION)
            .observacon(UPDATED_OBSERVACON)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);
        return trabajo;
    }

    @BeforeEach
    public void initTest() {
        trabajo = createEntity(em);
    }

    @Test
    @Transactional
    void createTrabajo() throws Exception {
        int databaseSizeBeforeCreate = trabajoRepository.findAll().size();
        // Create the Trabajo
        restTrabajoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trabajo)))
            .andExpect(status().isCreated());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeCreate + 1);
        Trabajo testTrabajo = trabajoList.get(trabajoList.size() - 1);
        assertThat(testTrabajo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTrabajo.getCargo()).isEqualTo(DEFAULT_CARGO);
        assertThat(testTrabajo.getSalario()).isEqualTo(DEFAULT_SALARIO);
        assertThat(testTrabajo.getSalud()).isEqualTo(DEFAULT_SALUD);
        assertThat(testTrabajo.getPension()).isEqualTo(DEFAULT_PENSION);
        assertThat(testTrabajo.getObservacon()).isEqualTo(DEFAULT_OBSERVACON);
        assertThat(testTrabajo.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createTrabajoWithExistingId() throws Exception {
        // Create the Trabajo with an existing ID
        trabajo.setId(1L);

        int databaseSizeBeforeCreate = trabajoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrabajoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trabajo)))
            .andExpect(status().isBadRequest());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrabajos() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

        // Get all the trabajoList
        restTrabajoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trabajo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].cargo").value(hasItem(DEFAULT_CARGO)))
            .andExpect(jsonPath("$.[*].salario").value(hasItem(DEFAULT_SALARIO)))
            .andExpect(jsonPath("$.[*].salud").value(hasItem(DEFAULT_SALUD)))
            .andExpect(jsonPath("$.[*].pension").value(hasItem(DEFAULT_PENSION)))
            .andExpect(jsonPath("$.[*].observacon").value(hasItem(DEFAULT_OBSERVACON)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO)));
    }

    @Test
    @Transactional
    void getTrabajo() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

        // Get the trabajo
        restTrabajoMockMvc
            .perform(get(ENTITY_API_URL_ID, trabajo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trabajo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.cargo").value(DEFAULT_CARGO))
            .andExpect(jsonPath("$.salario").value(DEFAULT_SALARIO))
            .andExpect(jsonPath("$.salud").value(DEFAULT_SALUD))
            .andExpect(jsonPath("$.pension").value(DEFAULT_PENSION))
            .andExpect(jsonPath("$.observacon").value(DEFAULT_OBSERVACON))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO));
    }

    @Test
    @Transactional
    void getNonExistingTrabajo() throws Exception {
        // Get the trabajo
        restTrabajoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTrabajo() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();

        // Update the trabajo
        Trabajo updatedTrabajo = trabajoRepository.findById(trabajo.getId()).get();
        // Disconnect from session so that the updates on updatedTrabajo are not directly saved in db
        em.detach(updatedTrabajo);
        updatedTrabajo
            .nombre(UPDATED_NOMBRE)
            .cargo(UPDATED_CARGO)
            .salario(UPDATED_SALARIO)
            .salud(UPDATED_SALUD)
            .pension(UPDATED_PENSION)
            .observacon(UPDATED_OBSERVACON)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restTrabajoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrabajo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTrabajo))
            )
            .andExpect(status().isOk());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate);
        Trabajo testTrabajo = trabajoList.get(trabajoList.size() - 1);
        assertThat(testTrabajo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTrabajo.getCargo()).isEqualTo(UPDATED_CARGO);
        assertThat(testTrabajo.getSalario()).isEqualTo(UPDATED_SALARIO);
        assertThat(testTrabajo.getSalud()).isEqualTo(UPDATED_SALUD);
        assertThat(testTrabajo.getPension()).isEqualTo(UPDATED_PENSION);
        assertThat(testTrabajo.getObservacon()).isEqualTo(UPDATED_OBSERVACON);
        assertThat(testTrabajo.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingTrabajo() throws Exception {
        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();
        trabajo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrabajoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trabajo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trabajo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrabajo() throws Exception {
        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();
        trabajo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trabajo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrabajo() throws Exception {
        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();
        trabajo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trabajo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrabajoWithPatch() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();

        // Update the trabajo using partial update
        Trabajo partialUpdatedTrabajo = new Trabajo();
        partialUpdatedTrabajo.setId(trabajo.getId());

        partialUpdatedTrabajo.nombre(UPDATED_NOMBRE).salario(UPDATED_SALARIO).salud(UPDATED_SALUD).observacon(UPDATED_OBSERVACON);

        restTrabajoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrabajo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrabajo))
            )
            .andExpect(status().isOk());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate);
        Trabajo testTrabajo = trabajoList.get(trabajoList.size() - 1);
        assertThat(testTrabajo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTrabajo.getCargo()).isEqualTo(DEFAULT_CARGO);
        assertThat(testTrabajo.getSalario()).isEqualTo(UPDATED_SALARIO);
        assertThat(testTrabajo.getSalud()).isEqualTo(UPDATED_SALUD);
        assertThat(testTrabajo.getPension()).isEqualTo(DEFAULT_PENSION);
        assertThat(testTrabajo.getObservacon()).isEqualTo(UPDATED_OBSERVACON);
        assertThat(testTrabajo.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateTrabajoWithPatch() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();

        // Update the trabajo using partial update
        Trabajo partialUpdatedTrabajo = new Trabajo();
        partialUpdatedTrabajo.setId(trabajo.getId());

        partialUpdatedTrabajo
            .nombre(UPDATED_NOMBRE)
            .cargo(UPDATED_CARGO)
            .salario(UPDATED_SALARIO)
            .salud(UPDATED_SALUD)
            .pension(UPDATED_PENSION)
            .observacon(UPDATED_OBSERVACON)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restTrabajoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrabajo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrabajo))
            )
            .andExpect(status().isOk());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate);
        Trabajo testTrabajo = trabajoList.get(trabajoList.size() - 1);
        assertThat(testTrabajo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTrabajo.getCargo()).isEqualTo(UPDATED_CARGO);
        assertThat(testTrabajo.getSalario()).isEqualTo(UPDATED_SALARIO);
        assertThat(testTrabajo.getSalud()).isEqualTo(UPDATED_SALUD);
        assertThat(testTrabajo.getPension()).isEqualTo(UPDATED_PENSION);
        assertThat(testTrabajo.getObservacon()).isEqualTo(UPDATED_OBSERVACON);
        assertThat(testTrabajo.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingTrabajo() throws Exception {
        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();
        trabajo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrabajoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trabajo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trabajo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrabajo() throws Exception {
        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();
        trabajo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trabajo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrabajo() throws Exception {
        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();
        trabajo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(trabajo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrabajo() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

        int databaseSizeBeforeDelete = trabajoRepository.findAll().size();

        // Delete the trabajo
        restTrabajoMockMvc
            .perform(delete(ENTITY_API_URL_ID, trabajo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
