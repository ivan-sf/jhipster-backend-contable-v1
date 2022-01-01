package com.ivansantander.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ivansantander.IntegrationTest;
import com.ivansantander.domain.Objeto;
import com.ivansantander.repository.ObjetoRepository;
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
 * Integration tests for the {@link ObjetoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ObjetoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final String DEFAULT_CODIGO_DIAN = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_DIAN = "BBBBBBBBBB";

    private static final Long DEFAULT_DETALLE_OBJETO = 1L;
    private static final Long UPDATED_DETALLE_OBJETO = 2L;

    private static final Instant DEFAULT_VENCIMIENTO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VENCIMIENTO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/objetos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjetoRepository objetoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restObjetoMockMvc;

    private Objeto objeto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Objeto createEntity(EntityManager em) {
        Objeto objeto = new Objeto()
            .nombre(DEFAULT_NOMBRE)
            .cantidad(DEFAULT_CANTIDAD)
            .codigoDian(DEFAULT_CODIGO_DIAN)
            .detalleObjeto(DEFAULT_DETALLE_OBJETO)
            .vencimiento(DEFAULT_VENCIMIENTO)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return objeto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Objeto createUpdatedEntity(EntityManager em) {
        Objeto objeto = new Objeto()
            .nombre(UPDATED_NOMBRE)
            .cantidad(UPDATED_CANTIDAD)
            .codigoDian(UPDATED_CODIGO_DIAN)
            .detalleObjeto(UPDATED_DETALLE_OBJETO)
            .vencimiento(UPDATED_VENCIMIENTO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);
        return objeto;
    }

    @BeforeEach
    public void initTest() {
        objeto = createEntity(em);
    }

    @Test
    @Transactional
    void createObjeto() throws Exception {
        int databaseSizeBeforeCreate = objetoRepository.findAll().size();
        // Create the Objeto
        restObjetoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(objeto)))
            .andExpect(status().isCreated());

        // Validate the Objeto in the database
        List<Objeto> objetoList = objetoRepository.findAll();
        assertThat(objetoList).hasSize(databaseSizeBeforeCreate + 1);
        Objeto testObjeto = objetoList.get(objetoList.size() - 1);
        assertThat(testObjeto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testObjeto.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testObjeto.getCodigoDian()).isEqualTo(DEFAULT_CODIGO_DIAN);
        assertThat(testObjeto.getDetalleObjeto()).isEqualTo(DEFAULT_DETALLE_OBJETO);
        assertThat(testObjeto.getVencimiento()).isEqualTo(DEFAULT_VENCIMIENTO);
        assertThat(testObjeto.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createObjetoWithExistingId() throws Exception {
        // Create the Objeto with an existing ID
        objeto.setId(1L);

        int databaseSizeBeforeCreate = objetoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restObjetoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(objeto)))
            .andExpect(status().isBadRequest());

        // Validate the Objeto in the database
        List<Objeto> objetoList = objetoRepository.findAll();
        assertThat(objetoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllObjetos() throws Exception {
        // Initialize the database
        objetoRepository.saveAndFlush(objeto);

        // Get all the objetoList
        restObjetoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(objeto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].codigoDian").value(hasItem(DEFAULT_CODIGO_DIAN)))
            .andExpect(jsonPath("$.[*].detalleObjeto").value(hasItem(DEFAULT_DETALLE_OBJETO.intValue())))
            .andExpect(jsonPath("$.[*].vencimiento").value(hasItem(DEFAULT_VENCIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @Test
    @Transactional
    void getObjeto() throws Exception {
        // Initialize the database
        objetoRepository.saveAndFlush(objeto);

        // Get the objeto
        restObjetoMockMvc
            .perform(get(ENTITY_API_URL_ID, objeto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(objeto.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.codigoDian").value(DEFAULT_CODIGO_DIAN))
            .andExpect(jsonPath("$.detalleObjeto").value(DEFAULT_DETALLE_OBJETO.intValue()))
            .andExpect(jsonPath("$.vencimiento").value(DEFAULT_VENCIMIENTO.toString()))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingObjeto() throws Exception {
        // Get the objeto
        restObjetoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewObjeto() throws Exception {
        // Initialize the database
        objetoRepository.saveAndFlush(objeto);

        int databaseSizeBeforeUpdate = objetoRepository.findAll().size();

        // Update the objeto
        Objeto updatedObjeto = objetoRepository.findById(objeto.getId()).get();
        // Disconnect from session so that the updates on updatedObjeto are not directly saved in db
        em.detach(updatedObjeto);
        updatedObjeto
            .nombre(UPDATED_NOMBRE)
            .cantidad(UPDATED_CANTIDAD)
            .codigoDian(UPDATED_CODIGO_DIAN)
            .detalleObjeto(UPDATED_DETALLE_OBJETO)
            .vencimiento(UPDATED_VENCIMIENTO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restObjetoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedObjeto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedObjeto))
            )
            .andExpect(status().isOk());

        // Validate the Objeto in the database
        List<Objeto> objetoList = objetoRepository.findAll();
        assertThat(objetoList).hasSize(databaseSizeBeforeUpdate);
        Objeto testObjeto = objetoList.get(objetoList.size() - 1);
        assertThat(testObjeto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testObjeto.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testObjeto.getCodigoDian()).isEqualTo(UPDATED_CODIGO_DIAN);
        assertThat(testObjeto.getDetalleObjeto()).isEqualTo(UPDATED_DETALLE_OBJETO);
        assertThat(testObjeto.getVencimiento()).isEqualTo(UPDATED_VENCIMIENTO);
        assertThat(testObjeto.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingObjeto() throws Exception {
        int databaseSizeBeforeUpdate = objetoRepository.findAll().size();
        objeto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObjetoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, objeto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(objeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objeto in the database
        List<Objeto> objetoList = objetoRepository.findAll();
        assertThat(objetoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchObjeto() throws Exception {
        int databaseSizeBeforeUpdate = objetoRepository.findAll().size();
        objeto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjetoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(objeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objeto in the database
        List<Objeto> objetoList = objetoRepository.findAll();
        assertThat(objetoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamObjeto() throws Exception {
        int databaseSizeBeforeUpdate = objetoRepository.findAll().size();
        objeto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjetoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(objeto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Objeto in the database
        List<Objeto> objetoList = objetoRepository.findAll();
        assertThat(objetoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateObjetoWithPatch() throws Exception {
        // Initialize the database
        objetoRepository.saveAndFlush(objeto);

        int databaseSizeBeforeUpdate = objetoRepository.findAll().size();

        // Update the objeto using partial update
        Objeto partialUpdatedObjeto = new Objeto();
        partialUpdatedObjeto.setId(objeto.getId());

        partialUpdatedObjeto.nombre(UPDATED_NOMBRE).cantidad(UPDATED_CANTIDAD).codigoDian(UPDATED_CODIGO_DIAN);

        restObjetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedObjeto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedObjeto))
            )
            .andExpect(status().isOk());

        // Validate the Objeto in the database
        List<Objeto> objetoList = objetoRepository.findAll();
        assertThat(objetoList).hasSize(databaseSizeBeforeUpdate);
        Objeto testObjeto = objetoList.get(objetoList.size() - 1);
        assertThat(testObjeto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testObjeto.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testObjeto.getCodigoDian()).isEqualTo(UPDATED_CODIGO_DIAN);
        assertThat(testObjeto.getDetalleObjeto()).isEqualTo(DEFAULT_DETALLE_OBJETO);
        assertThat(testObjeto.getVencimiento()).isEqualTo(DEFAULT_VENCIMIENTO);
        assertThat(testObjeto.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateObjetoWithPatch() throws Exception {
        // Initialize the database
        objetoRepository.saveAndFlush(objeto);

        int databaseSizeBeforeUpdate = objetoRepository.findAll().size();

        // Update the objeto using partial update
        Objeto partialUpdatedObjeto = new Objeto();
        partialUpdatedObjeto.setId(objeto.getId());

        partialUpdatedObjeto
            .nombre(UPDATED_NOMBRE)
            .cantidad(UPDATED_CANTIDAD)
            .codigoDian(UPDATED_CODIGO_DIAN)
            .detalleObjeto(UPDATED_DETALLE_OBJETO)
            .vencimiento(UPDATED_VENCIMIENTO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restObjetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedObjeto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedObjeto))
            )
            .andExpect(status().isOk());

        // Validate the Objeto in the database
        List<Objeto> objetoList = objetoRepository.findAll();
        assertThat(objetoList).hasSize(databaseSizeBeforeUpdate);
        Objeto testObjeto = objetoList.get(objetoList.size() - 1);
        assertThat(testObjeto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testObjeto.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testObjeto.getCodigoDian()).isEqualTo(UPDATED_CODIGO_DIAN);
        assertThat(testObjeto.getDetalleObjeto()).isEqualTo(UPDATED_DETALLE_OBJETO);
        assertThat(testObjeto.getVencimiento()).isEqualTo(UPDATED_VENCIMIENTO);
        assertThat(testObjeto.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingObjeto() throws Exception {
        int databaseSizeBeforeUpdate = objetoRepository.findAll().size();
        objeto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObjetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, objeto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(objeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objeto in the database
        List<Objeto> objetoList = objetoRepository.findAll();
        assertThat(objetoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchObjeto() throws Exception {
        int databaseSizeBeforeUpdate = objetoRepository.findAll().size();
        objeto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(objeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objeto in the database
        List<Objeto> objetoList = objetoRepository.findAll();
        assertThat(objetoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamObjeto() throws Exception {
        int databaseSizeBeforeUpdate = objetoRepository.findAll().size();
        objeto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjetoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(objeto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Objeto in the database
        List<Objeto> objetoList = objetoRepository.findAll();
        assertThat(objetoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteObjeto() throws Exception {
        // Initialize the database
        objetoRepository.saveAndFlush(objeto);

        int databaseSizeBeforeDelete = objetoRepository.findAll().size();

        // Delete the objeto
        restObjetoMockMvc
            .perform(delete(ENTITY_API_URL_ID, objeto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Objeto> objetoList = objetoRepository.findAll();
        assertThat(objetoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
