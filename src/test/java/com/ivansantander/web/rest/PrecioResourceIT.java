package com.ivansantander.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ivansantander.IntegrationTest;
import com.ivansantander.domain.Precio;
import com.ivansantander.repository.PrecioRepository;
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
 * Integration tests for the {@link PrecioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrecioResourceIT {

    private static final Double DEFAULT_VALOR_VENTA = 1D;
    private static final Double UPDATED_VALOR_VENTA = 2D;

    private static final Double DEFAULT_VALOR_COMPRA = 1D;
    private static final Double UPDATED_VALOR_COMPRA = 2D;

    private static final String ENTITY_API_URL = "/api/precios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrecioRepository precioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrecioMockMvc;

    private Precio precio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Precio createEntity(EntityManager em) {
        Precio precio = new Precio().valorVenta(DEFAULT_VALOR_VENTA).valorCompra(DEFAULT_VALOR_COMPRA);
        return precio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Precio createUpdatedEntity(EntityManager em) {
        Precio precio = new Precio().valorVenta(UPDATED_VALOR_VENTA).valorCompra(UPDATED_VALOR_COMPRA);
        return precio;
    }

    @BeforeEach
    public void initTest() {
        precio = createEntity(em);
    }

    @Test
    @Transactional
    void createPrecio() throws Exception {
        int databaseSizeBeforeCreate = precioRepository.findAll().size();
        // Create the Precio
        restPrecioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(precio)))
            .andExpect(status().isCreated());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeCreate + 1);
        Precio testPrecio = precioList.get(precioList.size() - 1);
        assertThat(testPrecio.getValorVenta()).isEqualTo(DEFAULT_VALOR_VENTA);
        assertThat(testPrecio.getValorCompra()).isEqualTo(DEFAULT_VALOR_COMPRA);
    }

    @Test
    @Transactional
    void createPrecioWithExistingId() throws Exception {
        // Create the Precio with an existing ID
        precio.setId(1L);

        int databaseSizeBeforeCreate = precioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrecioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(precio)))
            .andExpect(status().isBadRequest());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPrecios() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get all the precioList
        restPrecioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(precio.getId().intValue())))
            .andExpect(jsonPath("$.[*].valorVenta").value(hasItem(DEFAULT_VALOR_VENTA.doubleValue())))
            .andExpect(jsonPath("$.[*].valorCompra").value(hasItem(DEFAULT_VALOR_COMPRA.doubleValue())));
    }

    @Test
    @Transactional
    void getPrecio() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get the precio
        restPrecioMockMvc
            .perform(get(ENTITY_API_URL_ID, precio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(precio.getId().intValue()))
            .andExpect(jsonPath("$.valorVenta").value(DEFAULT_VALOR_VENTA.doubleValue()))
            .andExpect(jsonPath("$.valorCompra").value(DEFAULT_VALOR_COMPRA.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingPrecio() throws Exception {
        // Get the precio
        restPrecioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrecio() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        int databaseSizeBeforeUpdate = precioRepository.findAll().size();

        // Update the precio
        Precio updatedPrecio = precioRepository.findById(precio.getId()).get();
        // Disconnect from session so that the updates on updatedPrecio are not directly saved in db
        em.detach(updatedPrecio);
        updatedPrecio.valorVenta(UPDATED_VALOR_VENTA).valorCompra(UPDATED_VALOR_COMPRA);

        restPrecioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrecio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPrecio))
            )
            .andExpect(status().isOk());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeUpdate);
        Precio testPrecio = precioList.get(precioList.size() - 1);
        assertThat(testPrecio.getValorVenta()).isEqualTo(UPDATED_VALOR_VENTA);
        assertThat(testPrecio.getValorCompra()).isEqualTo(UPDATED_VALOR_COMPRA);
    }

    @Test
    @Transactional
    void putNonExistingPrecio() throws Exception {
        int databaseSizeBeforeUpdate = precioRepository.findAll().size();
        precio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrecioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, precio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(precio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrecio() throws Exception {
        int databaseSizeBeforeUpdate = precioRepository.findAll().size();
        precio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrecioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(precio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrecio() throws Exception {
        int databaseSizeBeforeUpdate = precioRepository.findAll().size();
        precio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrecioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(precio)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrecioWithPatch() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        int databaseSizeBeforeUpdate = precioRepository.findAll().size();

        // Update the precio using partial update
        Precio partialUpdatedPrecio = new Precio();
        partialUpdatedPrecio.setId(precio.getId());

        partialUpdatedPrecio.valorVenta(UPDATED_VALOR_VENTA);

        restPrecioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrecio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrecio))
            )
            .andExpect(status().isOk());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeUpdate);
        Precio testPrecio = precioList.get(precioList.size() - 1);
        assertThat(testPrecio.getValorVenta()).isEqualTo(UPDATED_VALOR_VENTA);
        assertThat(testPrecio.getValorCompra()).isEqualTo(DEFAULT_VALOR_COMPRA);
    }

    @Test
    @Transactional
    void fullUpdatePrecioWithPatch() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        int databaseSizeBeforeUpdate = precioRepository.findAll().size();

        // Update the precio using partial update
        Precio partialUpdatedPrecio = new Precio();
        partialUpdatedPrecio.setId(precio.getId());

        partialUpdatedPrecio.valorVenta(UPDATED_VALOR_VENTA).valorCompra(UPDATED_VALOR_COMPRA);

        restPrecioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrecio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrecio))
            )
            .andExpect(status().isOk());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeUpdate);
        Precio testPrecio = precioList.get(precioList.size() - 1);
        assertThat(testPrecio.getValorVenta()).isEqualTo(UPDATED_VALOR_VENTA);
        assertThat(testPrecio.getValorCompra()).isEqualTo(UPDATED_VALOR_COMPRA);
    }

    @Test
    @Transactional
    void patchNonExistingPrecio() throws Exception {
        int databaseSizeBeforeUpdate = precioRepository.findAll().size();
        precio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrecioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, precio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(precio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrecio() throws Exception {
        int databaseSizeBeforeUpdate = precioRepository.findAll().size();
        precio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrecioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(precio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrecio() throws Exception {
        int databaseSizeBeforeUpdate = precioRepository.findAll().size();
        precio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrecioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(precio)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrecio() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        int databaseSizeBeforeDelete = precioRepository.findAll().size();

        // Delete the precio
        restPrecioMockMvc
            .perform(delete(ENTITY_API_URL_ID, precio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
