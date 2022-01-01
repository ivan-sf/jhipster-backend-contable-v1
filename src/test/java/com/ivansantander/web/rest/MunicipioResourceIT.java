package com.ivansantander.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ivansantander.IntegrationTest;
import com.ivansantander.domain.Municipio;
import com.ivansantander.repository.MunicipioRepository;
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
 * Integration tests for the {@link MunicipioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MunicipioResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_DIAN = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_DIAN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/municipios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMunicipioMockMvc;

    private Municipio municipio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Municipio createEntity(EntityManager em) {
        Municipio municipio = new Municipio().nombre(DEFAULT_NOMBRE).codigoDIAN(DEFAULT_CODIGO_DIAN);
        return municipio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Municipio createUpdatedEntity(EntityManager em) {
        Municipio municipio = new Municipio().nombre(UPDATED_NOMBRE).codigoDIAN(UPDATED_CODIGO_DIAN);
        return municipio;
    }

    @BeforeEach
    public void initTest() {
        municipio = createEntity(em);
    }

    @Test
    @Transactional
    void createMunicipio() throws Exception {
        int databaseSizeBeforeCreate = municipioRepository.findAll().size();
        // Create the Municipio
        restMunicipioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(municipio)))
            .andExpect(status().isCreated());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeCreate + 1);
        Municipio testMunicipio = municipioList.get(municipioList.size() - 1);
        assertThat(testMunicipio.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testMunicipio.getCodigoDIAN()).isEqualTo(DEFAULT_CODIGO_DIAN);
    }

    @Test
    @Transactional
    void createMunicipioWithExistingId() throws Exception {
        // Create the Municipio with an existing ID
        municipio.setId(1L);

        int databaseSizeBeforeCreate = municipioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMunicipioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(municipio)))
            .andExpect(status().isBadRequest());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMunicipios() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        // Get all the municipioList
        restMunicipioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(municipio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].codigoDIAN").value(hasItem(DEFAULT_CODIGO_DIAN)));
    }

    @Test
    @Transactional
    void getMunicipio() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        // Get the municipio
        restMunicipioMockMvc
            .perform(get(ENTITY_API_URL_ID, municipio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(municipio.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.codigoDIAN").value(DEFAULT_CODIGO_DIAN));
    }

    @Test
    @Transactional
    void getNonExistingMunicipio() throws Exception {
        // Get the municipio
        restMunicipioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMunicipio() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();

        // Update the municipio
        Municipio updatedMunicipio = municipioRepository.findById(municipio.getId()).get();
        // Disconnect from session so that the updates on updatedMunicipio are not directly saved in db
        em.detach(updatedMunicipio);
        updatedMunicipio.nombre(UPDATED_NOMBRE).codigoDIAN(UPDATED_CODIGO_DIAN);

        restMunicipioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMunicipio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMunicipio))
            )
            .andExpect(status().isOk());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeUpdate);
        Municipio testMunicipio = municipioList.get(municipioList.size() - 1);
        assertThat(testMunicipio.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMunicipio.getCodigoDIAN()).isEqualTo(UPDATED_CODIGO_DIAN);
    }

    @Test
    @Transactional
    void putNonExistingMunicipio() throws Exception {
        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();
        municipio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMunicipioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, municipio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(municipio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMunicipio() throws Exception {
        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();
        municipio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMunicipioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(municipio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMunicipio() throws Exception {
        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();
        municipio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMunicipioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(municipio)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMunicipioWithPatch() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();

        // Update the municipio using partial update
        Municipio partialUpdatedMunicipio = new Municipio();
        partialUpdatedMunicipio.setId(municipio.getId());

        partialUpdatedMunicipio.nombre(UPDATED_NOMBRE).codigoDIAN(UPDATED_CODIGO_DIAN);

        restMunicipioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMunicipio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMunicipio))
            )
            .andExpect(status().isOk());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeUpdate);
        Municipio testMunicipio = municipioList.get(municipioList.size() - 1);
        assertThat(testMunicipio.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMunicipio.getCodigoDIAN()).isEqualTo(UPDATED_CODIGO_DIAN);
    }

    @Test
    @Transactional
    void fullUpdateMunicipioWithPatch() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();

        // Update the municipio using partial update
        Municipio partialUpdatedMunicipio = new Municipio();
        partialUpdatedMunicipio.setId(municipio.getId());

        partialUpdatedMunicipio.nombre(UPDATED_NOMBRE).codigoDIAN(UPDATED_CODIGO_DIAN);

        restMunicipioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMunicipio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMunicipio))
            )
            .andExpect(status().isOk());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeUpdate);
        Municipio testMunicipio = municipioList.get(municipioList.size() - 1);
        assertThat(testMunicipio.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMunicipio.getCodigoDIAN()).isEqualTo(UPDATED_CODIGO_DIAN);
    }

    @Test
    @Transactional
    void patchNonExistingMunicipio() throws Exception {
        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();
        municipio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMunicipioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, municipio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(municipio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMunicipio() throws Exception {
        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();
        municipio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMunicipioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(municipio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMunicipio() throws Exception {
        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();
        municipio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMunicipioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(municipio))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMunicipio() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        int databaseSizeBeforeDelete = municipioRepository.findAll().size();

        // Delete the municipio
        restMunicipioMockMvc
            .perform(delete(ENTITY_API_URL_ID, municipio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
