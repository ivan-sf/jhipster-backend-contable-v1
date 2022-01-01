package com.ivansantander.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ivansantander.IntegrationTest;
import com.ivansantander.domain.DetalleFactura;
import com.ivansantander.repository.DetalleFacturaRepository;
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
 * Integration tests for the {@link DetalleFacturaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DetalleFacturaResourceIT {

    private static final Double DEFAULT_PRECIO = 1D;
    private static final Double UPDATED_PRECIO = 2D;

    private static final Double DEFAULT_CANTIDAD = 1D;
    private static final Double UPDATED_CANTIDAD = 2D;

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;

    private static final Integer DEFAULT_IVA = 1;
    private static final Integer UPDATED_IVA = 2;

    private static final Double DEFAULT_VALOR_IMPUESTO = 1D;
    private static final Double UPDATED_VALOR_IMPUESTO = 2D;

    private static final Integer DEFAULT_ESTADO = 1;
    private static final Integer UPDATED_ESTADO = 2;

    private static final String ENTITY_API_URL = "/api/detalle-facturas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DetalleFacturaRepository detalleFacturaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetalleFacturaMockMvc;

    private DetalleFactura detalleFactura;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetalleFactura createEntity(EntityManager em) {
        DetalleFactura detalleFactura = new DetalleFactura()
            .precio(DEFAULT_PRECIO)
            .cantidad(DEFAULT_CANTIDAD)
            .total(DEFAULT_TOTAL)
            .iva(DEFAULT_IVA)
            .valorImpuesto(DEFAULT_VALOR_IMPUESTO)
            .estado(DEFAULT_ESTADO);
        return detalleFactura;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetalleFactura createUpdatedEntity(EntityManager em) {
        DetalleFactura detalleFactura = new DetalleFactura()
            .precio(UPDATED_PRECIO)
            .cantidad(UPDATED_CANTIDAD)
            .total(UPDATED_TOTAL)
            .iva(UPDATED_IVA)
            .valorImpuesto(UPDATED_VALOR_IMPUESTO)
            .estado(UPDATED_ESTADO);
        return detalleFactura;
    }

    @BeforeEach
    public void initTest() {
        detalleFactura = createEntity(em);
    }

    @Test
    @Transactional
    void createDetalleFactura() throws Exception {
        int databaseSizeBeforeCreate = detalleFacturaRepository.findAll().size();
        // Create the DetalleFactura
        restDetalleFacturaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detalleFactura))
            )
            .andExpect(status().isCreated());

        // Validate the DetalleFactura in the database
        List<DetalleFactura> detalleFacturaList = detalleFacturaRepository.findAll();
        assertThat(detalleFacturaList).hasSize(databaseSizeBeforeCreate + 1);
        DetalleFactura testDetalleFactura = detalleFacturaList.get(detalleFacturaList.size() - 1);
        assertThat(testDetalleFactura.getPrecio()).isEqualTo(DEFAULT_PRECIO);
        assertThat(testDetalleFactura.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testDetalleFactura.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testDetalleFactura.getIva()).isEqualTo(DEFAULT_IVA);
        assertThat(testDetalleFactura.getValorImpuesto()).isEqualTo(DEFAULT_VALOR_IMPUESTO);
        assertThat(testDetalleFactura.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createDetalleFacturaWithExistingId() throws Exception {
        // Create the DetalleFactura with an existing ID
        detalleFactura.setId(1L);

        int databaseSizeBeforeCreate = detalleFacturaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetalleFacturaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detalleFactura))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleFactura in the database
        List<DetalleFactura> detalleFacturaList = detalleFacturaRepository.findAll();
        assertThat(detalleFacturaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDetalleFacturas() throws Exception {
        // Initialize the database
        detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList
        restDetalleFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detalleFactura.getId().intValue())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD.doubleValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].iva").value(hasItem(DEFAULT_IVA)))
            .andExpect(jsonPath("$.[*].valorImpuesto").value(hasItem(DEFAULT_VALOR_IMPUESTO.doubleValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));
    }

    @Test
    @Transactional
    void getDetalleFactura() throws Exception {
        // Initialize the database
        detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get the detalleFactura
        restDetalleFacturaMockMvc
            .perform(get(ENTITY_API_URL_ID, detalleFactura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detalleFactura.getId().intValue()))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.doubleValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD.doubleValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.iva").value(DEFAULT_IVA))
            .andExpect(jsonPath("$.valorImpuesto").value(DEFAULT_VALOR_IMPUESTO.doubleValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO));
    }

    @Test
    @Transactional
    void getNonExistingDetalleFactura() throws Exception {
        // Get the detalleFactura
        restDetalleFacturaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDetalleFactura() throws Exception {
        // Initialize the database
        detalleFacturaRepository.saveAndFlush(detalleFactura);

        int databaseSizeBeforeUpdate = detalleFacturaRepository.findAll().size();

        // Update the detalleFactura
        DetalleFactura updatedDetalleFactura = detalleFacturaRepository.findById(detalleFactura.getId()).get();
        // Disconnect from session so that the updates on updatedDetalleFactura are not directly saved in db
        em.detach(updatedDetalleFactura);
        updatedDetalleFactura
            .precio(UPDATED_PRECIO)
            .cantidad(UPDATED_CANTIDAD)
            .total(UPDATED_TOTAL)
            .iva(UPDATED_IVA)
            .valorImpuesto(UPDATED_VALOR_IMPUESTO)
            .estado(UPDATED_ESTADO);

        restDetalleFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDetalleFactura.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDetalleFactura))
            )
            .andExpect(status().isOk());

        // Validate the DetalleFactura in the database
        List<DetalleFactura> detalleFacturaList = detalleFacturaRepository.findAll();
        assertThat(detalleFacturaList).hasSize(databaseSizeBeforeUpdate);
        DetalleFactura testDetalleFactura = detalleFacturaList.get(detalleFacturaList.size() - 1);
        assertThat(testDetalleFactura.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testDetalleFactura.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testDetalleFactura.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testDetalleFactura.getIva()).isEqualTo(UPDATED_IVA);
        assertThat(testDetalleFactura.getValorImpuesto()).isEqualTo(UPDATED_VALOR_IMPUESTO);
        assertThat(testDetalleFactura.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingDetalleFactura() throws Exception {
        int databaseSizeBeforeUpdate = detalleFacturaRepository.findAll().size();
        detalleFactura.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetalleFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detalleFactura.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detalleFactura))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleFactura in the database
        List<DetalleFactura> detalleFacturaList = detalleFacturaRepository.findAll();
        assertThat(detalleFacturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDetalleFactura() throws Exception {
        int databaseSizeBeforeUpdate = detalleFacturaRepository.findAll().size();
        detalleFactura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detalleFactura))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleFactura in the database
        List<DetalleFactura> detalleFacturaList = detalleFacturaRepository.findAll();
        assertThat(detalleFacturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDetalleFactura() throws Exception {
        int databaseSizeBeforeUpdate = detalleFacturaRepository.findAll().size();
        detalleFactura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleFacturaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detalleFactura)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetalleFactura in the database
        List<DetalleFactura> detalleFacturaList = detalleFacturaRepository.findAll();
        assertThat(detalleFacturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDetalleFacturaWithPatch() throws Exception {
        // Initialize the database
        detalleFacturaRepository.saveAndFlush(detalleFactura);

        int databaseSizeBeforeUpdate = detalleFacturaRepository.findAll().size();

        // Update the detalleFactura using partial update
        DetalleFactura partialUpdatedDetalleFactura = new DetalleFactura();
        partialUpdatedDetalleFactura.setId(detalleFactura.getId());

        partialUpdatedDetalleFactura.precio(UPDATED_PRECIO).cantidad(UPDATED_CANTIDAD).total(UPDATED_TOTAL);

        restDetalleFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetalleFactura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetalleFactura))
            )
            .andExpect(status().isOk());

        // Validate the DetalleFactura in the database
        List<DetalleFactura> detalleFacturaList = detalleFacturaRepository.findAll();
        assertThat(detalleFacturaList).hasSize(databaseSizeBeforeUpdate);
        DetalleFactura testDetalleFactura = detalleFacturaList.get(detalleFacturaList.size() - 1);
        assertThat(testDetalleFactura.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testDetalleFactura.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testDetalleFactura.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testDetalleFactura.getIva()).isEqualTo(DEFAULT_IVA);
        assertThat(testDetalleFactura.getValorImpuesto()).isEqualTo(DEFAULT_VALOR_IMPUESTO);
        assertThat(testDetalleFactura.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateDetalleFacturaWithPatch() throws Exception {
        // Initialize the database
        detalleFacturaRepository.saveAndFlush(detalleFactura);

        int databaseSizeBeforeUpdate = detalleFacturaRepository.findAll().size();

        // Update the detalleFactura using partial update
        DetalleFactura partialUpdatedDetalleFactura = new DetalleFactura();
        partialUpdatedDetalleFactura.setId(detalleFactura.getId());

        partialUpdatedDetalleFactura
            .precio(UPDATED_PRECIO)
            .cantidad(UPDATED_CANTIDAD)
            .total(UPDATED_TOTAL)
            .iva(UPDATED_IVA)
            .valorImpuesto(UPDATED_VALOR_IMPUESTO)
            .estado(UPDATED_ESTADO);

        restDetalleFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetalleFactura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetalleFactura))
            )
            .andExpect(status().isOk());

        // Validate the DetalleFactura in the database
        List<DetalleFactura> detalleFacturaList = detalleFacturaRepository.findAll();
        assertThat(detalleFacturaList).hasSize(databaseSizeBeforeUpdate);
        DetalleFactura testDetalleFactura = detalleFacturaList.get(detalleFacturaList.size() - 1);
        assertThat(testDetalleFactura.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testDetalleFactura.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testDetalleFactura.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testDetalleFactura.getIva()).isEqualTo(UPDATED_IVA);
        assertThat(testDetalleFactura.getValorImpuesto()).isEqualTo(UPDATED_VALOR_IMPUESTO);
        assertThat(testDetalleFactura.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingDetalleFactura() throws Exception {
        int databaseSizeBeforeUpdate = detalleFacturaRepository.findAll().size();
        detalleFactura.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetalleFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, detalleFactura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detalleFactura))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleFactura in the database
        List<DetalleFactura> detalleFacturaList = detalleFacturaRepository.findAll();
        assertThat(detalleFacturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDetalleFactura() throws Exception {
        int databaseSizeBeforeUpdate = detalleFacturaRepository.findAll().size();
        detalleFactura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detalleFactura))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleFactura in the database
        List<DetalleFactura> detalleFacturaList = detalleFacturaRepository.findAll();
        assertThat(detalleFacturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDetalleFactura() throws Exception {
        int databaseSizeBeforeUpdate = detalleFacturaRepository.findAll().size();
        detalleFactura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(detalleFactura))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetalleFactura in the database
        List<DetalleFactura> detalleFacturaList = detalleFacturaRepository.findAll();
        assertThat(detalleFacturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDetalleFactura() throws Exception {
        // Initialize the database
        detalleFacturaRepository.saveAndFlush(detalleFactura);

        int databaseSizeBeforeDelete = detalleFacturaRepository.findAll().size();

        // Delete the detalleFactura
        restDetalleFacturaMockMvc
            .perform(delete(ENTITY_API_URL_ID, detalleFactura.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DetalleFactura> detalleFacturaList = detalleFacturaRepository.findAll();
        assertThat(detalleFacturaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
