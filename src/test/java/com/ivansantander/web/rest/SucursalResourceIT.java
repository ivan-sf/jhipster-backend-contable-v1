package com.ivansantander.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ivansantander.IntegrationTest;
import com.ivansantander.domain.Sucursal;
import com.ivansantander.repository.SucursalRepository;
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
 * Integration tests for the {@link SucursalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SucursalResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final Integer DEFAULT_INDICATIVO = 1;
    private static final Integer UPDATED_INDICATIVO = 2;

    private static final Integer DEFAULT_TELEFONO = 1;
    private static final Integer UPDATED_TELEFONO = 2;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final String DEFAULT_RESOLUCION_FACTURAS = "AAAAAAAAAA";
    private static final String UPDATED_RESOLUCION_FACTURAS = "BBBBBBBBBB";

    private static final String DEFAULT_PREFIJO_INICIAL = "AAAAAAAAAA";
    private static final String UPDATED_PREFIJO_INICIAL = "BBBBBBBBBB";

    private static final String DEFAULT_PREFIJO_FINAL = "AAAAAAAAAA";
    private static final String UPDATED_PREFIJO_FINAL = "BBBBBBBBBB";

    private static final String DEFAULT_PREFIJO_ACTUAL = "AAAAAAAAAA";
    private static final String UPDATED_PREFIJO_ACTUAL = "BBBBBBBBBB";

    private static final Long DEFAULT_DESCRIPCION = 1L;
    private static final Long UPDATED_DESCRIPCION = 2L;

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/sucursals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSucursalMockMvc;

    private Sucursal sucursal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sucursal createEntity(EntityManager em) {
        Sucursal sucursal = new Sucursal()
            .nombre(DEFAULT_NOMBRE)
            .direccion(DEFAULT_DIRECCION)
            .indicativo(DEFAULT_INDICATIVO)
            .telefono(DEFAULT_TELEFONO)
            .email(DEFAULT_EMAIL)
            .logo(DEFAULT_LOGO)
            .resolucionFacturas(DEFAULT_RESOLUCION_FACTURAS)
            .prefijoInicial(DEFAULT_PREFIJO_INICIAL)
            .prefijoFinal(DEFAULT_PREFIJO_FINAL)
            .prefijoActual(DEFAULT_PREFIJO_ACTUAL)
            .descripcion(DEFAULT_DESCRIPCION)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return sucursal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sucursal createUpdatedEntity(EntityManager em) {
        Sucursal sucursal = new Sucursal()
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .indicativo(UPDATED_INDICATIVO)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .logo(UPDATED_LOGO)
            .resolucionFacturas(UPDATED_RESOLUCION_FACTURAS)
            .prefijoInicial(UPDATED_PREFIJO_INICIAL)
            .prefijoFinal(UPDATED_PREFIJO_FINAL)
            .prefijoActual(UPDATED_PREFIJO_ACTUAL)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);
        return sucursal;
    }

    @BeforeEach
    public void initTest() {
        sucursal = createEntity(em);
    }

    @Test
    @Transactional
    void createSucursal() throws Exception {
        int databaseSizeBeforeCreate = sucursalRepository.findAll().size();
        // Create the Sucursal
        restSucursalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sucursal)))
            .andExpect(status().isCreated());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeCreate + 1);
        Sucursal testSucursal = sucursalList.get(sucursalList.size() - 1);
        assertThat(testSucursal.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testSucursal.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testSucursal.getIndicativo()).isEqualTo(DEFAULT_INDICATIVO);
        assertThat(testSucursal.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testSucursal.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSucursal.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testSucursal.getResolucionFacturas()).isEqualTo(DEFAULT_RESOLUCION_FACTURAS);
        assertThat(testSucursal.getPrefijoInicial()).isEqualTo(DEFAULT_PREFIJO_INICIAL);
        assertThat(testSucursal.getPrefijoFinal()).isEqualTo(DEFAULT_PREFIJO_FINAL);
        assertThat(testSucursal.getPrefijoActual()).isEqualTo(DEFAULT_PREFIJO_ACTUAL);
        assertThat(testSucursal.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testSucursal.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createSucursalWithExistingId() throws Exception {
        // Create the Sucursal with an existing ID
        sucursal.setId(1L);

        int databaseSizeBeforeCreate = sucursalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSucursalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sucursal)))
            .andExpect(status().isBadRequest());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSucursals() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList
        restSucursalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sucursal.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].indicativo").value(hasItem(DEFAULT_INDICATIVO)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.[*].resolucionFacturas").value(hasItem(DEFAULT_RESOLUCION_FACTURAS)))
            .andExpect(jsonPath("$.[*].prefijoInicial").value(hasItem(DEFAULT_PREFIJO_INICIAL)))
            .andExpect(jsonPath("$.[*].prefijoFinal").value(hasItem(DEFAULT_PREFIJO_FINAL)))
            .andExpect(jsonPath("$.[*].prefijoActual").value(hasItem(DEFAULT_PREFIJO_ACTUAL)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.intValue())))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @Test
    @Transactional
    void getSucursal() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get the sucursal
        restSucursalMockMvc
            .perform(get(ENTITY_API_URL_ID, sucursal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sucursal.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.indicativo").value(DEFAULT_INDICATIVO))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO))
            .andExpect(jsonPath("$.resolucionFacturas").value(DEFAULT_RESOLUCION_FACTURAS))
            .andExpect(jsonPath("$.prefijoInicial").value(DEFAULT_PREFIJO_INICIAL))
            .andExpect(jsonPath("$.prefijoFinal").value(DEFAULT_PREFIJO_FINAL))
            .andExpect(jsonPath("$.prefijoActual").value(DEFAULT_PREFIJO_ACTUAL))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.intValue()))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSucursal() throws Exception {
        // Get the sucursal
        restSucursalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSucursal() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();

        // Update the sucursal
        Sucursal updatedSucursal = sucursalRepository.findById(sucursal.getId()).get();
        // Disconnect from session so that the updates on updatedSucursal are not directly saved in db
        em.detach(updatedSucursal);
        updatedSucursal
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .indicativo(UPDATED_INDICATIVO)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .logo(UPDATED_LOGO)
            .resolucionFacturas(UPDATED_RESOLUCION_FACTURAS)
            .prefijoInicial(UPDATED_PREFIJO_INICIAL)
            .prefijoFinal(UPDATED_PREFIJO_FINAL)
            .prefijoActual(UPDATED_PREFIJO_ACTUAL)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restSucursalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSucursal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSucursal))
            )
            .andExpect(status().isOk());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
        Sucursal testSucursal = sucursalList.get(sucursalList.size() - 1);
        assertThat(testSucursal.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSucursal.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testSucursal.getIndicativo()).isEqualTo(UPDATED_INDICATIVO);
        assertThat(testSucursal.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testSucursal.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSucursal.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testSucursal.getResolucionFacturas()).isEqualTo(UPDATED_RESOLUCION_FACTURAS);
        assertThat(testSucursal.getPrefijoInicial()).isEqualTo(UPDATED_PREFIJO_INICIAL);
        assertThat(testSucursal.getPrefijoFinal()).isEqualTo(UPDATED_PREFIJO_FINAL);
        assertThat(testSucursal.getPrefijoActual()).isEqualTo(UPDATED_PREFIJO_ACTUAL);
        assertThat(testSucursal.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testSucursal.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingSucursal() throws Exception {
        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();
        sucursal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSucursalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sucursal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sucursal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSucursal() throws Exception {
        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();
        sucursal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSucursalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sucursal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSucursal() throws Exception {
        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();
        sucursal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSucursalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sucursal)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSucursalWithPatch() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();

        // Update the sucursal using partial update
        Sucursal partialUpdatedSucursal = new Sucursal();
        partialUpdatedSucursal.setId(sucursal.getId());

        partialUpdatedSucursal
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .resolucionFacturas(UPDATED_RESOLUCION_FACTURAS)
            .prefijoFinal(UPDATED_PREFIJO_FINAL)
            .prefijoActual(UPDATED_PREFIJO_ACTUAL)
            .descripcion(UPDATED_DESCRIPCION);

        restSucursalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSucursal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSucursal))
            )
            .andExpect(status().isOk());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
        Sucursal testSucursal = sucursalList.get(sucursalList.size() - 1);
        assertThat(testSucursal.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSucursal.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testSucursal.getIndicativo()).isEqualTo(DEFAULT_INDICATIVO);
        assertThat(testSucursal.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testSucursal.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSucursal.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testSucursal.getResolucionFacturas()).isEqualTo(UPDATED_RESOLUCION_FACTURAS);
        assertThat(testSucursal.getPrefijoInicial()).isEqualTo(DEFAULT_PREFIJO_INICIAL);
        assertThat(testSucursal.getPrefijoFinal()).isEqualTo(UPDATED_PREFIJO_FINAL);
        assertThat(testSucursal.getPrefijoActual()).isEqualTo(UPDATED_PREFIJO_ACTUAL);
        assertThat(testSucursal.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testSucursal.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateSucursalWithPatch() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();

        // Update the sucursal using partial update
        Sucursal partialUpdatedSucursal = new Sucursal();
        partialUpdatedSucursal.setId(sucursal.getId());

        partialUpdatedSucursal
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .indicativo(UPDATED_INDICATIVO)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .logo(UPDATED_LOGO)
            .resolucionFacturas(UPDATED_RESOLUCION_FACTURAS)
            .prefijoInicial(UPDATED_PREFIJO_INICIAL)
            .prefijoFinal(UPDATED_PREFIJO_FINAL)
            .prefijoActual(UPDATED_PREFIJO_ACTUAL)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restSucursalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSucursal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSucursal))
            )
            .andExpect(status().isOk());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
        Sucursal testSucursal = sucursalList.get(sucursalList.size() - 1);
        assertThat(testSucursal.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSucursal.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testSucursal.getIndicativo()).isEqualTo(UPDATED_INDICATIVO);
        assertThat(testSucursal.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testSucursal.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSucursal.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testSucursal.getResolucionFacturas()).isEqualTo(UPDATED_RESOLUCION_FACTURAS);
        assertThat(testSucursal.getPrefijoInicial()).isEqualTo(UPDATED_PREFIJO_INICIAL);
        assertThat(testSucursal.getPrefijoFinal()).isEqualTo(UPDATED_PREFIJO_FINAL);
        assertThat(testSucursal.getPrefijoActual()).isEqualTo(UPDATED_PREFIJO_ACTUAL);
        assertThat(testSucursal.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testSucursal.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingSucursal() throws Exception {
        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();
        sucursal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSucursalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sucursal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sucursal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSucursal() throws Exception {
        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();
        sucursal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSucursalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sucursal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSucursal() throws Exception {
        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();
        sucursal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSucursalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sucursal)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSucursal() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        int databaseSizeBeforeDelete = sucursalRepository.findAll().size();

        // Delete the sucursal
        restSucursalMockMvc
            .perform(delete(ENTITY_API_URL_ID, sucursal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
