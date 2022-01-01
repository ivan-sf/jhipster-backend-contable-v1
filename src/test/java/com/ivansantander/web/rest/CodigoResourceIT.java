package com.ivansantander.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ivansantander.IntegrationTest;
import com.ivansantander.domain.Codigo;
import com.ivansantander.repository.CodigoRepository;
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
 * Integration tests for the {@link CodigoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CodigoResourceIT {

    private static final Long DEFAULT_BAR_CODE = 1L;
    private static final Long UPDATED_BAR_CODE = 2L;

    private static final Long DEFAULT_QR_CODE = 1L;
    private static final Long UPDATED_QR_CODE = 2L;

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/codigos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CodigoRepository codigoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCodigoMockMvc;

    private Codigo codigo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Codigo createEntity(EntityManager em) {
        Codigo codigo = new Codigo()
            .barCode(DEFAULT_BAR_CODE)
            .qrCode(DEFAULT_QR_CODE)
            .cantidad(DEFAULT_CANTIDAD)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return codigo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Codigo createUpdatedEntity(EntityManager em) {
        Codigo codigo = new Codigo()
            .barCode(UPDATED_BAR_CODE)
            .qrCode(UPDATED_QR_CODE)
            .cantidad(UPDATED_CANTIDAD)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);
        return codigo;
    }

    @BeforeEach
    public void initTest() {
        codigo = createEntity(em);
    }

    @Test
    @Transactional
    void createCodigo() throws Exception {
        int databaseSizeBeforeCreate = codigoRepository.findAll().size();
        // Create the Codigo
        restCodigoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codigo)))
            .andExpect(status().isCreated());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeCreate + 1);
        Codigo testCodigo = codigoList.get(codigoList.size() - 1);
        assertThat(testCodigo.getBarCode()).isEqualTo(DEFAULT_BAR_CODE);
        assertThat(testCodigo.getQrCode()).isEqualTo(DEFAULT_QR_CODE);
        assertThat(testCodigo.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testCodigo.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createCodigoWithExistingId() throws Exception {
        // Create the Codigo with an existing ID
        codigo.setId(1L);

        int databaseSizeBeforeCreate = codigoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCodigoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codigo)))
            .andExpect(status().isBadRequest());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCodigos() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        // Get all the codigoList
        restCodigoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(codigo.getId().intValue())))
            .andExpect(jsonPath("$.[*].barCode").value(hasItem(DEFAULT_BAR_CODE.intValue())))
            .andExpect(jsonPath("$.[*].qrCode").value(hasItem(DEFAULT_QR_CODE.intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @Test
    @Transactional
    void getCodigo() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        // Get the codigo
        restCodigoMockMvc
            .perform(get(ENTITY_API_URL_ID, codigo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(codigo.getId().intValue()))
            .andExpect(jsonPath("$.barCode").value(DEFAULT_BAR_CODE.intValue()))
            .andExpect(jsonPath("$.qrCode").value(DEFAULT_QR_CODE.intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCodigo() throws Exception {
        // Get the codigo
        restCodigoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCodigo() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        int databaseSizeBeforeUpdate = codigoRepository.findAll().size();

        // Update the codigo
        Codigo updatedCodigo = codigoRepository.findById(codigo.getId()).get();
        // Disconnect from session so that the updates on updatedCodigo are not directly saved in db
        em.detach(updatedCodigo);
        updatedCodigo.barCode(UPDATED_BAR_CODE).qrCode(UPDATED_QR_CODE).cantidad(UPDATED_CANTIDAD).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restCodigoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCodigo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCodigo))
            )
            .andExpect(status().isOk());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeUpdate);
        Codigo testCodigo = codigoList.get(codigoList.size() - 1);
        assertThat(testCodigo.getBarCode()).isEqualTo(UPDATED_BAR_CODE);
        assertThat(testCodigo.getQrCode()).isEqualTo(UPDATED_QR_CODE);
        assertThat(testCodigo.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testCodigo.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingCodigo() throws Exception {
        int databaseSizeBeforeUpdate = codigoRepository.findAll().size();
        codigo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodigoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, codigo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(codigo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCodigo() throws Exception {
        int databaseSizeBeforeUpdate = codigoRepository.findAll().size();
        codigo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodigoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(codigo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCodigo() throws Exception {
        int databaseSizeBeforeUpdate = codigoRepository.findAll().size();
        codigo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodigoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codigo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCodigoWithPatch() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        int databaseSizeBeforeUpdate = codigoRepository.findAll().size();

        // Update the codigo using partial update
        Codigo partialUpdatedCodigo = new Codigo();
        partialUpdatedCodigo.setId(codigo.getId());

        partialUpdatedCodigo
            .barCode(UPDATED_BAR_CODE)
            .qrCode(UPDATED_QR_CODE)
            .cantidad(UPDATED_CANTIDAD)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restCodigoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodigo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCodigo))
            )
            .andExpect(status().isOk());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeUpdate);
        Codigo testCodigo = codigoList.get(codigoList.size() - 1);
        assertThat(testCodigo.getBarCode()).isEqualTo(UPDATED_BAR_CODE);
        assertThat(testCodigo.getQrCode()).isEqualTo(UPDATED_QR_CODE);
        assertThat(testCodigo.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testCodigo.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateCodigoWithPatch() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        int databaseSizeBeforeUpdate = codigoRepository.findAll().size();

        // Update the codigo using partial update
        Codigo partialUpdatedCodigo = new Codigo();
        partialUpdatedCodigo.setId(codigo.getId());

        partialUpdatedCodigo
            .barCode(UPDATED_BAR_CODE)
            .qrCode(UPDATED_QR_CODE)
            .cantidad(UPDATED_CANTIDAD)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restCodigoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodigo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCodigo))
            )
            .andExpect(status().isOk());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeUpdate);
        Codigo testCodigo = codigoList.get(codigoList.size() - 1);
        assertThat(testCodigo.getBarCode()).isEqualTo(UPDATED_BAR_CODE);
        assertThat(testCodigo.getQrCode()).isEqualTo(UPDATED_QR_CODE);
        assertThat(testCodigo.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testCodigo.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingCodigo() throws Exception {
        int databaseSizeBeforeUpdate = codigoRepository.findAll().size();
        codigo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodigoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, codigo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(codigo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCodigo() throws Exception {
        int databaseSizeBeforeUpdate = codigoRepository.findAll().size();
        codigo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodigoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(codigo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCodigo() throws Exception {
        int databaseSizeBeforeUpdate = codigoRepository.findAll().size();
        codigo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodigoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(codigo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCodigo() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        int databaseSizeBeforeDelete = codigoRepository.findAll().size();

        // Delete the codigo
        restCodigoMockMvc
            .perform(delete(ENTITY_API_URL_ID, codigo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
