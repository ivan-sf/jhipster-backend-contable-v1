package com.ivansantander.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ivansantander.IntegrationTest;
import com.ivansantander.domain.Empresa;
import com.ivansantander.repository.EmpresaRepository;
import com.ivansantander.service.EmpresaService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmpresaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmpresaResourceIT {

    private static final String DEFAULT_RAZON_SOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_RAZON_SOCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_COMERCIAL = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_COMERCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_DOCUMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_DV = "AAAAAAAAAA";
    private static final String UPDATED_DV = "BBBBBBBBBB";

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

    private static final Integer DEFAULT_ESTADO = 1;
    private static final Integer UPDATED_ESTADO = 2;

    private static final String ENTITY_API_URL = "/api/empresas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmpresaRepository empresaRepository;

    @Mock
    private EmpresaRepository empresaRepositoryMock;

    @Mock
    private EmpresaService empresaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmpresaMockMvc;

    private Empresa empresa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empresa createEntity(EntityManager em) {
        Empresa empresa = new Empresa()
            .razonSocial(DEFAULT_RAZON_SOCIAL)
            .nombreComercial(DEFAULT_NOMBRE_COMERCIAL)
            .tipoDocumento(DEFAULT_TIPO_DOCUMENTO)
            .documento(DEFAULT_DOCUMENTO)
            .dv(DEFAULT_DV)
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
            .fechaRegistro(DEFAULT_FECHA_REGISTRO)
            .estado(DEFAULT_ESTADO);
        return empresa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empresa createUpdatedEntity(EntityManager em) {
        Empresa empresa = new Empresa()
            .razonSocial(UPDATED_RAZON_SOCIAL)
            .nombreComercial(UPDATED_NOMBRE_COMERCIAL)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .documento(UPDATED_DOCUMENTO)
            .dv(UPDATED_DV)
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
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .estado(UPDATED_ESTADO);
        return empresa;
    }

    @BeforeEach
    public void initTest() {
        empresa = createEntity(em);
    }

    @Test
    @Transactional
    void createEmpresa() throws Exception {
        int databaseSizeBeforeCreate = empresaRepository.findAll().size();
        // Create the Empresa
        restEmpresaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empresa)))
            .andExpect(status().isCreated());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeCreate + 1);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getRazonSocial()).isEqualTo(DEFAULT_RAZON_SOCIAL);
        assertThat(testEmpresa.getNombreComercial()).isEqualTo(DEFAULT_NOMBRE_COMERCIAL);
        assertThat(testEmpresa.getTipoDocumento()).isEqualTo(DEFAULT_TIPO_DOCUMENTO);
        assertThat(testEmpresa.getDocumento()).isEqualTo(DEFAULT_DOCUMENTO);
        assertThat(testEmpresa.getDv()).isEqualTo(DEFAULT_DV);
        assertThat(testEmpresa.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testEmpresa.getIndicativo()).isEqualTo(DEFAULT_INDICATIVO);
        assertThat(testEmpresa.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testEmpresa.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmpresa.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testEmpresa.getResolucionFacturas()).isEqualTo(DEFAULT_RESOLUCION_FACTURAS);
        assertThat(testEmpresa.getPrefijoInicial()).isEqualTo(DEFAULT_PREFIJO_INICIAL);
        assertThat(testEmpresa.getPrefijoFinal()).isEqualTo(DEFAULT_PREFIJO_FINAL);
        assertThat(testEmpresa.getPrefijoActual()).isEqualTo(DEFAULT_PREFIJO_ACTUAL);
        assertThat(testEmpresa.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testEmpresa.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
        assertThat(testEmpresa.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createEmpresaWithExistingId() throws Exception {
        // Create the Empresa with an existing ID
        empresa.setId(1L);

        int databaseSizeBeforeCreate = empresaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmpresaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empresa)))
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmpresas() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList
        restEmpresaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empresa.getId().intValue())))
            .andExpect(jsonPath("$.[*].razonSocial").value(hasItem(DEFAULT_RAZON_SOCIAL)))
            .andExpect(jsonPath("$.[*].nombreComercial").value(hasItem(DEFAULT_NOMBRE_COMERCIAL)))
            .andExpect(jsonPath("$.[*].tipoDocumento").value(hasItem(DEFAULT_TIPO_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].documento").value(hasItem(DEFAULT_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].dv").value(hasItem(DEFAULT_DV)))
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
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmpresasWithEagerRelationshipsIsEnabled() throws Exception {
        when(empresaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmpresaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(empresaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmpresasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(empresaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmpresaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(empresaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getEmpresa() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get the empresa
        restEmpresaMockMvc
            .perform(get(ENTITY_API_URL_ID, empresa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(empresa.getId().intValue()))
            .andExpect(jsonPath("$.razonSocial").value(DEFAULT_RAZON_SOCIAL))
            .andExpect(jsonPath("$.nombreComercial").value(DEFAULT_NOMBRE_COMERCIAL))
            .andExpect(jsonPath("$.tipoDocumento").value(DEFAULT_TIPO_DOCUMENTO))
            .andExpect(jsonPath("$.documento").value(DEFAULT_DOCUMENTO))
            .andExpect(jsonPath("$.dv").value(DEFAULT_DV))
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
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO));
    }

    @Test
    @Transactional
    void getNonExistingEmpresa() throws Exception {
        // Get the empresa
        restEmpresaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmpresa() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();

        // Update the empresa
        Empresa updatedEmpresa = empresaRepository.findById(empresa.getId()).get();
        // Disconnect from session so that the updates on updatedEmpresa are not directly saved in db
        em.detach(updatedEmpresa);
        updatedEmpresa
            .razonSocial(UPDATED_RAZON_SOCIAL)
            .nombreComercial(UPDATED_NOMBRE_COMERCIAL)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .documento(UPDATED_DOCUMENTO)
            .dv(UPDATED_DV)
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
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .estado(UPDATED_ESTADO);

        restEmpresaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmpresa.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmpresa))
            )
            .andExpect(status().isOk());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getRazonSocial()).isEqualTo(UPDATED_RAZON_SOCIAL);
        assertThat(testEmpresa.getNombreComercial()).isEqualTo(UPDATED_NOMBRE_COMERCIAL);
        assertThat(testEmpresa.getTipoDocumento()).isEqualTo(UPDATED_TIPO_DOCUMENTO);
        assertThat(testEmpresa.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
        assertThat(testEmpresa.getDv()).isEqualTo(UPDATED_DV);
        assertThat(testEmpresa.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testEmpresa.getIndicativo()).isEqualTo(UPDATED_INDICATIVO);
        assertThat(testEmpresa.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testEmpresa.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmpresa.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testEmpresa.getResolucionFacturas()).isEqualTo(UPDATED_RESOLUCION_FACTURAS);
        assertThat(testEmpresa.getPrefijoInicial()).isEqualTo(UPDATED_PREFIJO_INICIAL);
        assertThat(testEmpresa.getPrefijoFinal()).isEqualTo(UPDATED_PREFIJO_FINAL);
        assertThat(testEmpresa.getPrefijoActual()).isEqualTo(UPDATED_PREFIJO_ACTUAL);
        assertThat(testEmpresa.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testEmpresa.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
        assertThat(testEmpresa.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();
        empresa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empresa.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(empresa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();
        empresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(empresa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();
        empresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empresa)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmpresaWithPatch() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();

        // Update the empresa using partial update
        Empresa partialUpdatedEmpresa = new Empresa();
        partialUpdatedEmpresa.setId(empresa.getId());

        partialUpdatedEmpresa
            .razonSocial(UPDATED_RAZON_SOCIAL)
            .nombreComercial(UPDATED_NOMBRE_COMERCIAL)
            .documento(UPDATED_DOCUMENTO)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .estado(UPDATED_ESTADO);

        restEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpresa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmpresa))
            )
            .andExpect(status().isOk());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getRazonSocial()).isEqualTo(UPDATED_RAZON_SOCIAL);
        assertThat(testEmpresa.getNombreComercial()).isEqualTo(UPDATED_NOMBRE_COMERCIAL);
        assertThat(testEmpresa.getTipoDocumento()).isEqualTo(DEFAULT_TIPO_DOCUMENTO);
        assertThat(testEmpresa.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
        assertThat(testEmpresa.getDv()).isEqualTo(DEFAULT_DV);
        assertThat(testEmpresa.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testEmpresa.getIndicativo()).isEqualTo(DEFAULT_INDICATIVO);
        assertThat(testEmpresa.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testEmpresa.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmpresa.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testEmpresa.getResolucionFacturas()).isEqualTo(DEFAULT_RESOLUCION_FACTURAS);
        assertThat(testEmpresa.getPrefijoInicial()).isEqualTo(DEFAULT_PREFIJO_INICIAL);
        assertThat(testEmpresa.getPrefijoFinal()).isEqualTo(DEFAULT_PREFIJO_FINAL);
        assertThat(testEmpresa.getPrefijoActual()).isEqualTo(DEFAULT_PREFIJO_ACTUAL);
        assertThat(testEmpresa.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testEmpresa.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
        assertThat(testEmpresa.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateEmpresaWithPatch() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();

        // Update the empresa using partial update
        Empresa partialUpdatedEmpresa = new Empresa();
        partialUpdatedEmpresa.setId(empresa.getId());

        partialUpdatedEmpresa
            .razonSocial(UPDATED_RAZON_SOCIAL)
            .nombreComercial(UPDATED_NOMBRE_COMERCIAL)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .documento(UPDATED_DOCUMENTO)
            .dv(UPDATED_DV)
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
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .estado(UPDATED_ESTADO);

        restEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpresa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmpresa))
            )
            .andExpect(status().isOk());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getRazonSocial()).isEqualTo(UPDATED_RAZON_SOCIAL);
        assertThat(testEmpresa.getNombreComercial()).isEqualTo(UPDATED_NOMBRE_COMERCIAL);
        assertThat(testEmpresa.getTipoDocumento()).isEqualTo(UPDATED_TIPO_DOCUMENTO);
        assertThat(testEmpresa.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
        assertThat(testEmpresa.getDv()).isEqualTo(UPDATED_DV);
        assertThat(testEmpresa.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testEmpresa.getIndicativo()).isEqualTo(UPDATED_INDICATIVO);
        assertThat(testEmpresa.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testEmpresa.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmpresa.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testEmpresa.getResolucionFacturas()).isEqualTo(UPDATED_RESOLUCION_FACTURAS);
        assertThat(testEmpresa.getPrefijoInicial()).isEqualTo(UPDATED_PREFIJO_INICIAL);
        assertThat(testEmpresa.getPrefijoFinal()).isEqualTo(UPDATED_PREFIJO_FINAL);
        assertThat(testEmpresa.getPrefijoActual()).isEqualTo(UPDATED_PREFIJO_ACTUAL);
        assertThat(testEmpresa.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testEmpresa.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
        assertThat(testEmpresa.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();
        empresa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, empresa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(empresa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();
        empresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(empresa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();
        empresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(empresa)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmpresa() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        int databaseSizeBeforeDelete = empresaRepository.findAll().size();

        // Delete the empresa
        restEmpresaMockMvc
            .perform(delete(ENTITY_API_URL_ID, empresa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
