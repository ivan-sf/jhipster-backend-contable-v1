package com.ivansantander.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ivansantander.IntegrationTest;
import com.ivansantander.domain.Factura;
import com.ivansantander.repository.FacturaRepository;
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
 * Integration tests for the {@link FacturaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FacturaResourceIT {

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    private static final String DEFAULT_CAJA = "AAAAAAAAAA";
    private static final String UPDATED_CAJA = "BBBBBBBBBB";

    private static final Integer DEFAULT_ESTADO = 1;
    private static final Integer UPDATED_ESTADO = 2;

    private static final Double DEFAULT_IVA_19 = 1D;
    private static final Double UPDATED_IVA_19 = 2D;

    private static final Double DEFAULT_BASE_IVA_19 = 1D;
    private static final Double UPDATED_BASE_IVA_19 = 2D;

    private static final Double DEFAULT_IVA_5 = 1D;
    private static final Double UPDATED_IVA_5 = 2D;

    private static final Double DEFAULT_BASE_IVA_5 = 1D;
    private static final Double UPDATED_BASE_IVA_5 = 2D;

    private static final Double DEFAULT_IVA_0 = 1D;
    private static final Double UPDATED_IVA_0 = 2D;

    private static final Double DEFAULT_BASE_IVA_0 = 1D;
    private static final Double UPDATED_BASE_IVA_0 = 2D;

    private static final Integer DEFAULT_TOTAL = 1;
    private static final Integer UPDATED_TOTAL = 2;

    private static final Double DEFAULT_PAGO = 1D;
    private static final Double UPDATED_PAGO = 2D;

    private static final Double DEFAULT_SALDO = 1D;
    private static final Double UPDATED_SALDO = 2D;

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_ACTUALIZACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_ACTUALIZACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/facturas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacturaMockMvc;

    private Factura factura;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factura createEntity(EntityManager em) {
        Factura factura = new Factura()
            .numero(DEFAULT_NUMERO)
            .caja(DEFAULT_CAJA)
            .estado(DEFAULT_ESTADO)
            .iva19(DEFAULT_IVA_19)
            .baseIva19(DEFAULT_BASE_IVA_19)
            .iva5(DEFAULT_IVA_5)
            .baseIva5(DEFAULT_BASE_IVA_5)
            .iva0(DEFAULT_IVA_0)
            .baseIva0(DEFAULT_BASE_IVA_0)
            .total(DEFAULT_TOTAL)
            .pago(DEFAULT_PAGO)
            .saldo(DEFAULT_SALDO)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO)
            .fechaActualizacion(DEFAULT_FECHA_ACTUALIZACION);
        return factura;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factura createUpdatedEntity(EntityManager em) {
        Factura factura = new Factura()
            .numero(UPDATED_NUMERO)
            .caja(UPDATED_CAJA)
            .estado(UPDATED_ESTADO)
            .iva19(UPDATED_IVA_19)
            .baseIva19(UPDATED_BASE_IVA_19)
            .iva5(UPDATED_IVA_5)
            .baseIva5(UPDATED_BASE_IVA_5)
            .iva0(UPDATED_IVA_0)
            .baseIva0(UPDATED_BASE_IVA_0)
            .total(UPDATED_TOTAL)
            .pago(UPDATED_PAGO)
            .saldo(UPDATED_SALDO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .fechaActualizacion(UPDATED_FECHA_ACTUALIZACION);
        return factura;
    }

    @BeforeEach
    public void initTest() {
        factura = createEntity(em);
    }

    @Test
    @Transactional
    void createFactura() throws Exception {
        int databaseSizeBeforeCreate = facturaRepository.findAll().size();
        // Create the Factura
        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isCreated());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeCreate + 1);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testFactura.getCaja()).isEqualTo(DEFAULT_CAJA);
        assertThat(testFactura.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testFactura.getIva19()).isEqualTo(DEFAULT_IVA_19);
        assertThat(testFactura.getBaseIva19()).isEqualTo(DEFAULT_BASE_IVA_19);
        assertThat(testFactura.getIva5()).isEqualTo(DEFAULT_IVA_5);
        assertThat(testFactura.getBaseIva5()).isEqualTo(DEFAULT_BASE_IVA_5);
        assertThat(testFactura.getIva0()).isEqualTo(DEFAULT_IVA_0);
        assertThat(testFactura.getBaseIva0()).isEqualTo(DEFAULT_BASE_IVA_0);
        assertThat(testFactura.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testFactura.getPago()).isEqualTo(DEFAULT_PAGO);
        assertThat(testFactura.getSaldo()).isEqualTo(DEFAULT_SALDO);
        assertThat(testFactura.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
        assertThat(testFactura.getFechaActualizacion()).isEqualTo(DEFAULT_FECHA_ACTUALIZACION);
    }

    @Test
    @Transactional
    void createFacturaWithExistingId() throws Exception {
        // Create the Factura with an existing ID
        factura.setId(1L);

        int databaseSizeBeforeCreate = facturaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFacturas() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factura.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].caja").value(hasItem(DEFAULT_CAJA)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].iva19").value(hasItem(DEFAULT_IVA_19.doubleValue())))
            .andExpect(jsonPath("$.[*].baseIva19").value(hasItem(DEFAULT_BASE_IVA_19.doubleValue())))
            .andExpect(jsonPath("$.[*].iva5").value(hasItem(DEFAULT_IVA_5.doubleValue())))
            .andExpect(jsonPath("$.[*].baseIva5").value(hasItem(DEFAULT_BASE_IVA_5.doubleValue())))
            .andExpect(jsonPath("$.[*].iva0").value(hasItem(DEFAULT_IVA_0.doubleValue())))
            .andExpect(jsonPath("$.[*].baseIva0").value(hasItem(DEFAULT_BASE_IVA_0.doubleValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.[*].pago").value(hasItem(DEFAULT_PAGO.doubleValue())))
            .andExpect(jsonPath("$.[*].saldo").value(hasItem(DEFAULT_SALDO.doubleValue())))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())))
            .andExpect(jsonPath("$.[*].fechaActualizacion").value(hasItem(DEFAULT_FECHA_ACTUALIZACION.toString())));
    }

    @Test
    @Transactional
    void getFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get the factura
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL_ID, factura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factura.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.caja").value(DEFAULT_CAJA))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.iva19").value(DEFAULT_IVA_19.doubleValue()))
            .andExpect(jsonPath("$.baseIva19").value(DEFAULT_BASE_IVA_19.doubleValue()))
            .andExpect(jsonPath("$.iva5").value(DEFAULT_IVA_5.doubleValue()))
            .andExpect(jsonPath("$.baseIva5").value(DEFAULT_BASE_IVA_5.doubleValue()))
            .andExpect(jsonPath("$.iva0").value(DEFAULT_IVA_0.doubleValue()))
            .andExpect(jsonPath("$.baseIva0").value(DEFAULT_BASE_IVA_0.doubleValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL))
            .andExpect(jsonPath("$.pago").value(DEFAULT_PAGO.doubleValue()))
            .andExpect(jsonPath("$.saldo").value(DEFAULT_SALDO.doubleValue()))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()))
            .andExpect(jsonPath("$.fechaActualizacion").value(DEFAULT_FECHA_ACTUALIZACION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFactura() throws Exception {
        // Get the factura
        restFacturaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Update the factura
        Factura updatedFactura = facturaRepository.findById(factura.getId()).get();
        // Disconnect from session so that the updates on updatedFactura are not directly saved in db
        em.detach(updatedFactura);
        updatedFactura
            .numero(UPDATED_NUMERO)
            .caja(UPDATED_CAJA)
            .estado(UPDATED_ESTADO)
            .iva19(UPDATED_IVA_19)
            .baseIva19(UPDATED_BASE_IVA_19)
            .iva5(UPDATED_IVA_5)
            .baseIva5(UPDATED_BASE_IVA_5)
            .iva0(UPDATED_IVA_0)
            .baseIva0(UPDATED_BASE_IVA_0)
            .total(UPDATED_TOTAL)
            .pago(UPDATED_PAGO)
            .saldo(UPDATED_SALDO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .fechaActualizacion(UPDATED_FECHA_ACTUALIZACION);

        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFactura.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFactura))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testFactura.getCaja()).isEqualTo(UPDATED_CAJA);
        assertThat(testFactura.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testFactura.getIva19()).isEqualTo(UPDATED_IVA_19);
        assertThat(testFactura.getBaseIva19()).isEqualTo(UPDATED_BASE_IVA_19);
        assertThat(testFactura.getIva5()).isEqualTo(UPDATED_IVA_5);
        assertThat(testFactura.getBaseIva5()).isEqualTo(UPDATED_BASE_IVA_5);
        assertThat(testFactura.getIva0()).isEqualTo(UPDATED_IVA_0);
        assertThat(testFactura.getBaseIva0()).isEqualTo(UPDATED_BASE_IVA_0);
        assertThat(testFactura.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testFactura.getPago()).isEqualTo(UPDATED_PAGO);
        assertThat(testFactura.getSaldo()).isEqualTo(UPDATED_SALDO);
        assertThat(testFactura.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
        assertThat(testFactura.getFechaActualizacion()).isEqualTo(UPDATED_FECHA_ACTUALIZACION);
    }

    @Test
    @Transactional
    void putNonExistingFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factura.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacturaWithPatch() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Update the factura using partial update
        Factura partialUpdatedFactura = new Factura();
        partialUpdatedFactura.setId(factura.getId());

        partialUpdatedFactura
            .estado(UPDATED_ESTADO)
            .iva19(UPDATED_IVA_19)
            .baseIva19(UPDATED_BASE_IVA_19)
            .iva5(UPDATED_IVA_5)
            .baseIva0(UPDATED_BASE_IVA_0);

        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactura))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testFactura.getCaja()).isEqualTo(DEFAULT_CAJA);
        assertThat(testFactura.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testFactura.getIva19()).isEqualTo(UPDATED_IVA_19);
        assertThat(testFactura.getBaseIva19()).isEqualTo(UPDATED_BASE_IVA_19);
        assertThat(testFactura.getIva5()).isEqualTo(UPDATED_IVA_5);
        assertThat(testFactura.getBaseIva5()).isEqualTo(DEFAULT_BASE_IVA_5);
        assertThat(testFactura.getIva0()).isEqualTo(DEFAULT_IVA_0);
        assertThat(testFactura.getBaseIva0()).isEqualTo(UPDATED_BASE_IVA_0);
        assertThat(testFactura.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testFactura.getPago()).isEqualTo(DEFAULT_PAGO);
        assertThat(testFactura.getSaldo()).isEqualTo(DEFAULT_SALDO);
        assertThat(testFactura.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
        assertThat(testFactura.getFechaActualizacion()).isEqualTo(DEFAULT_FECHA_ACTUALIZACION);
    }

    @Test
    @Transactional
    void fullUpdateFacturaWithPatch() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Update the factura using partial update
        Factura partialUpdatedFactura = new Factura();
        partialUpdatedFactura.setId(factura.getId());

        partialUpdatedFactura
            .numero(UPDATED_NUMERO)
            .caja(UPDATED_CAJA)
            .estado(UPDATED_ESTADO)
            .iva19(UPDATED_IVA_19)
            .baseIva19(UPDATED_BASE_IVA_19)
            .iva5(UPDATED_IVA_5)
            .baseIva5(UPDATED_BASE_IVA_5)
            .iva0(UPDATED_IVA_0)
            .baseIva0(UPDATED_BASE_IVA_0)
            .total(UPDATED_TOTAL)
            .pago(UPDATED_PAGO)
            .saldo(UPDATED_SALDO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .fechaActualizacion(UPDATED_FECHA_ACTUALIZACION);

        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactura))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testFactura.getCaja()).isEqualTo(UPDATED_CAJA);
        assertThat(testFactura.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testFactura.getIva19()).isEqualTo(UPDATED_IVA_19);
        assertThat(testFactura.getBaseIva19()).isEqualTo(UPDATED_BASE_IVA_19);
        assertThat(testFactura.getIva5()).isEqualTo(UPDATED_IVA_5);
        assertThat(testFactura.getBaseIva5()).isEqualTo(UPDATED_BASE_IVA_5);
        assertThat(testFactura.getIva0()).isEqualTo(UPDATED_IVA_0);
        assertThat(testFactura.getBaseIva0()).isEqualTo(UPDATED_BASE_IVA_0);
        assertThat(testFactura.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testFactura.getPago()).isEqualTo(UPDATED_PAGO);
        assertThat(testFactura.getSaldo()).isEqualTo(UPDATED_SALDO);
        assertThat(testFactura.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
        assertThat(testFactura.getFechaActualizacion()).isEqualTo(UPDATED_FECHA_ACTUALIZACION);
    }

    @Test
    @Transactional
    void patchNonExistingFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeDelete = facturaRepository.findAll().size();

        // Delete the factura
        restFacturaMockMvc
            .perform(delete(ENTITY_API_URL_ID, factura.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
