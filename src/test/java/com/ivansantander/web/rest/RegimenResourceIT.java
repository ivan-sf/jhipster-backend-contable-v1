package com.ivansantander.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ivansantander.IntegrationTest;
import com.ivansantander.domain.Regimen;
import com.ivansantander.repository.RegimenRepository;
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
 * Integration tests for the {@link RegimenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RegimenResourceIT {

    private static final String DEFAULT_TIPO_REGIMEN = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_REGIMEN = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_REGIMEN = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_REGIMEN = "BBBBBBBBBB";

    private static final String DEFAULT_FECHA_REGISTRO = "AAAAAAAAAA";
    private static final String UPDATED_FECHA_REGISTRO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/regimen";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RegimenRepository regimenRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegimenMockMvc;

    private Regimen regimen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Regimen createEntity(EntityManager em) {
        Regimen regimen = new Regimen()
            .tipoRegimen(DEFAULT_TIPO_REGIMEN)
            .nombreRegimen(DEFAULT_NOMBRE_REGIMEN)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return regimen;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Regimen createUpdatedEntity(EntityManager em) {
        Regimen regimen = new Regimen()
            .tipoRegimen(UPDATED_TIPO_REGIMEN)
            .nombreRegimen(UPDATED_NOMBRE_REGIMEN)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);
        return regimen;
    }

    @BeforeEach
    public void initTest() {
        regimen = createEntity(em);
    }

    @Test
    @Transactional
    void createRegimen() throws Exception {
        int databaseSizeBeforeCreate = regimenRepository.findAll().size();
        // Create the Regimen
        restRegimenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(regimen)))
            .andExpect(status().isCreated());

        // Validate the Regimen in the database
        List<Regimen> regimenList = regimenRepository.findAll();
        assertThat(regimenList).hasSize(databaseSizeBeforeCreate + 1);
        Regimen testRegimen = regimenList.get(regimenList.size() - 1);
        assertThat(testRegimen.getTipoRegimen()).isEqualTo(DEFAULT_TIPO_REGIMEN);
        assertThat(testRegimen.getNombreRegimen()).isEqualTo(DEFAULT_NOMBRE_REGIMEN);
        assertThat(testRegimen.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createRegimenWithExistingId() throws Exception {
        // Create the Regimen with an existing ID
        regimen.setId(1L);

        int databaseSizeBeforeCreate = regimenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegimenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(regimen)))
            .andExpect(status().isBadRequest());

        // Validate the Regimen in the database
        List<Regimen> regimenList = regimenRepository.findAll();
        assertThat(regimenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRegimen() throws Exception {
        // Initialize the database
        regimenRepository.saveAndFlush(regimen);

        // Get all the regimenList
        restRegimenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regimen.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoRegimen").value(hasItem(DEFAULT_TIPO_REGIMEN)))
            .andExpect(jsonPath("$.[*].nombreRegimen").value(hasItem(DEFAULT_NOMBRE_REGIMEN)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO)));
    }

    @Test
    @Transactional
    void getRegimen() throws Exception {
        // Initialize the database
        regimenRepository.saveAndFlush(regimen);

        // Get the regimen
        restRegimenMockMvc
            .perform(get(ENTITY_API_URL_ID, regimen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(regimen.getId().intValue()))
            .andExpect(jsonPath("$.tipoRegimen").value(DEFAULT_TIPO_REGIMEN))
            .andExpect(jsonPath("$.nombreRegimen").value(DEFAULT_NOMBRE_REGIMEN))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO));
    }

    @Test
    @Transactional
    void getNonExistingRegimen() throws Exception {
        // Get the regimen
        restRegimenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRegimen() throws Exception {
        // Initialize the database
        regimenRepository.saveAndFlush(regimen);

        int databaseSizeBeforeUpdate = regimenRepository.findAll().size();

        // Update the regimen
        Regimen updatedRegimen = regimenRepository.findById(regimen.getId()).get();
        // Disconnect from session so that the updates on updatedRegimen are not directly saved in db
        em.detach(updatedRegimen);
        updatedRegimen.tipoRegimen(UPDATED_TIPO_REGIMEN).nombreRegimen(UPDATED_NOMBRE_REGIMEN).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restRegimenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRegimen.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRegimen))
            )
            .andExpect(status().isOk());

        // Validate the Regimen in the database
        List<Regimen> regimenList = regimenRepository.findAll();
        assertThat(regimenList).hasSize(databaseSizeBeforeUpdate);
        Regimen testRegimen = regimenList.get(regimenList.size() - 1);
        assertThat(testRegimen.getTipoRegimen()).isEqualTo(UPDATED_TIPO_REGIMEN);
        assertThat(testRegimen.getNombreRegimen()).isEqualTo(UPDATED_NOMBRE_REGIMEN);
        assertThat(testRegimen.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingRegimen() throws Exception {
        int databaseSizeBeforeUpdate = regimenRepository.findAll().size();
        regimen.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegimenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, regimen.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(regimen))
            )
            .andExpect(status().isBadRequest());

        // Validate the Regimen in the database
        List<Regimen> regimenList = regimenRepository.findAll();
        assertThat(regimenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRegimen() throws Exception {
        int databaseSizeBeforeUpdate = regimenRepository.findAll().size();
        regimen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegimenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(regimen))
            )
            .andExpect(status().isBadRequest());

        // Validate the Regimen in the database
        List<Regimen> regimenList = regimenRepository.findAll();
        assertThat(regimenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRegimen() throws Exception {
        int databaseSizeBeforeUpdate = regimenRepository.findAll().size();
        regimen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegimenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(regimen)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Regimen in the database
        List<Regimen> regimenList = regimenRepository.findAll();
        assertThat(regimenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRegimenWithPatch() throws Exception {
        // Initialize the database
        regimenRepository.saveAndFlush(regimen);

        int databaseSizeBeforeUpdate = regimenRepository.findAll().size();

        // Update the regimen using partial update
        Regimen partialUpdatedRegimen = new Regimen();
        partialUpdatedRegimen.setId(regimen.getId());

        partialUpdatedRegimen.fechaRegistro(UPDATED_FECHA_REGISTRO);

        restRegimenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegimen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegimen))
            )
            .andExpect(status().isOk());

        // Validate the Regimen in the database
        List<Regimen> regimenList = regimenRepository.findAll();
        assertThat(regimenList).hasSize(databaseSizeBeforeUpdate);
        Regimen testRegimen = regimenList.get(regimenList.size() - 1);
        assertThat(testRegimen.getTipoRegimen()).isEqualTo(DEFAULT_TIPO_REGIMEN);
        assertThat(testRegimen.getNombreRegimen()).isEqualTo(DEFAULT_NOMBRE_REGIMEN);
        assertThat(testRegimen.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateRegimenWithPatch() throws Exception {
        // Initialize the database
        regimenRepository.saveAndFlush(regimen);

        int databaseSizeBeforeUpdate = regimenRepository.findAll().size();

        // Update the regimen using partial update
        Regimen partialUpdatedRegimen = new Regimen();
        partialUpdatedRegimen.setId(regimen.getId());

        partialUpdatedRegimen.tipoRegimen(UPDATED_TIPO_REGIMEN).nombreRegimen(UPDATED_NOMBRE_REGIMEN).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restRegimenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegimen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegimen))
            )
            .andExpect(status().isOk());

        // Validate the Regimen in the database
        List<Regimen> regimenList = regimenRepository.findAll();
        assertThat(regimenList).hasSize(databaseSizeBeforeUpdate);
        Regimen testRegimen = regimenList.get(regimenList.size() - 1);
        assertThat(testRegimen.getTipoRegimen()).isEqualTo(UPDATED_TIPO_REGIMEN);
        assertThat(testRegimen.getNombreRegimen()).isEqualTo(UPDATED_NOMBRE_REGIMEN);
        assertThat(testRegimen.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingRegimen() throws Exception {
        int databaseSizeBeforeUpdate = regimenRepository.findAll().size();
        regimen.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegimenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, regimen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(regimen))
            )
            .andExpect(status().isBadRequest());

        // Validate the Regimen in the database
        List<Regimen> regimenList = regimenRepository.findAll();
        assertThat(regimenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRegimen() throws Exception {
        int databaseSizeBeforeUpdate = regimenRepository.findAll().size();
        regimen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegimenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(regimen))
            )
            .andExpect(status().isBadRequest());

        // Validate the Regimen in the database
        List<Regimen> regimenList = regimenRepository.findAll();
        assertThat(regimenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRegimen() throws Exception {
        int databaseSizeBeforeUpdate = regimenRepository.findAll().size();
        regimen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegimenMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(regimen)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Regimen in the database
        List<Regimen> regimenList = regimenRepository.findAll();
        assertThat(regimenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRegimen() throws Exception {
        // Initialize the database
        regimenRepository.saveAndFlush(regimen);

        int databaseSizeBeforeDelete = regimenRepository.findAll().size();

        // Delete the regimen
        restRegimenMockMvc
            .perform(delete(ENTITY_API_URL_ID, regimen.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Regimen> regimenList = regimenRepository.findAll();
        assertThat(regimenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
