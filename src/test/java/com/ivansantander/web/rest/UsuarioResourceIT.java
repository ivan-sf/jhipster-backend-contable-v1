package com.ivansantander.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ivansantander.IntegrationTest;
import com.ivansantander.domain.Usuario;
import com.ivansantander.repository.UsuarioRepository;
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
 * Integration tests for the {@link UsuarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UsuarioResourceIT {

    private static final String DEFAULT_NOMBRES = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRES = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO_PERSONA = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_PERSONA = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_DOCUMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_DV = "AAAAAAAAAA";
    private static final String UPDATED_DV = "BBBBBBBBBB";

    private static final Integer DEFAULT_ESTADO = 1;
    private static final Integer UPDATED_ESTADO = 2;

    private static final String DEFAULT_ROL = "AAAAAAAAAA";
    private static final String UPDATED_ROL = "BBBBBBBBBB";

    private static final String DEFAULT_RUT = "AAAAAAAAAA";
    private static final String UPDATED_RUT = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_COMERCIAL = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_COMERCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_NIT = "AAAAAAAAAA";
    private static final String UPDATED_NIT = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final Integer DEFAULT_INDICATIVO = 1;
    private static final Integer UPDATED_INDICATIVO = 2;

    private static final Integer DEFAULT_TELEFONO = 1;
    private static final Integer UPDATED_TELEFONO = 2;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_GENERO = "AAAAAAAAAA";
    private static final String UPDATED_GENERO = "BBBBBBBBBB";

    private static final Instant DEFAULT_EDAD = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EDAD = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_FOTO = "AAAAAAAAAA";
    private static final String UPDATED_FOTO = "BBBBBBBBBB";

    private static final Long DEFAULT_DESCRIPCION = 1L;
    private static final Long UPDATED_DESCRIPCION = 2L;

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsuarioMockMvc;

    private Usuario usuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .nombres(DEFAULT_NOMBRES)
            .apellidos(DEFAULT_APELLIDOS)
            .tipoPersona(DEFAULT_TIPO_PERSONA)
            .tipoDocumento(DEFAULT_TIPO_DOCUMENTO)
            .documento(DEFAULT_DOCUMENTO)
            .dv(DEFAULT_DV)
            .estado(DEFAULT_ESTADO)
            .rol(DEFAULT_ROL)
            .rut(DEFAULT_RUT)
            .nombreComercial(DEFAULT_NOMBRE_COMERCIAL)
            .nit(DEFAULT_NIT)
            .direccion(DEFAULT_DIRECCION)
            .indicativo(DEFAULT_INDICATIVO)
            .telefono(DEFAULT_TELEFONO)
            .email(DEFAULT_EMAIL)
            .genero(DEFAULT_GENERO)
            .edad(DEFAULT_EDAD)
            .foto(DEFAULT_FOTO)
            .descripcion(DEFAULT_DESCRIPCION)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return usuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createUpdatedEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .tipoPersona(UPDATED_TIPO_PERSONA)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .documento(UPDATED_DOCUMENTO)
            .dv(UPDATED_DV)
            .estado(UPDATED_ESTADO)
            .rol(UPDATED_ROL)
            .rut(UPDATED_RUT)
            .nombreComercial(UPDATED_NOMBRE_COMERCIAL)
            .nit(UPDATED_NIT)
            .direccion(UPDATED_DIRECCION)
            .indicativo(UPDATED_INDICATIVO)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .genero(UPDATED_GENERO)
            .edad(UPDATED_EDAD)
            .foto(UPDATED_FOTO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);
        return usuario;
    }

    @BeforeEach
    public void initTest() {
        usuario = createEntity(em);
    }

    @Test
    @Transactional
    void createUsuario() throws Exception {
        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();
        // Create the Usuario
        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuario)))
            .andExpect(status().isCreated());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate + 1);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNombres()).isEqualTo(DEFAULT_NOMBRES);
        assertThat(testUsuario.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testUsuario.getTipoPersona()).isEqualTo(DEFAULT_TIPO_PERSONA);
        assertThat(testUsuario.getTipoDocumento()).isEqualTo(DEFAULT_TIPO_DOCUMENTO);
        assertThat(testUsuario.getDocumento()).isEqualTo(DEFAULT_DOCUMENTO);
        assertThat(testUsuario.getDv()).isEqualTo(DEFAULT_DV);
        assertThat(testUsuario.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testUsuario.getRol()).isEqualTo(DEFAULT_ROL);
        assertThat(testUsuario.getRut()).isEqualTo(DEFAULT_RUT);
        assertThat(testUsuario.getNombreComercial()).isEqualTo(DEFAULT_NOMBRE_COMERCIAL);
        assertThat(testUsuario.getNit()).isEqualTo(DEFAULT_NIT);
        assertThat(testUsuario.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testUsuario.getIndicativo()).isEqualTo(DEFAULT_INDICATIVO);
        assertThat(testUsuario.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testUsuario.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUsuario.getGenero()).isEqualTo(DEFAULT_GENERO);
        assertThat(testUsuario.getEdad()).isEqualTo(DEFAULT_EDAD);
        assertThat(testUsuario.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testUsuario.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testUsuario.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createUsuarioWithExistingId() throws Exception {
        // Create the Usuario with an existing ID
        usuario.setId(1L);

        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuario)))
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUsuarios() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombres").value(hasItem(DEFAULT_NOMBRES)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].tipoPersona").value(hasItem(DEFAULT_TIPO_PERSONA)))
            .andExpect(jsonPath("$.[*].tipoDocumento").value(hasItem(DEFAULT_TIPO_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].documento").value(hasItem(DEFAULT_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].dv").value(hasItem(DEFAULT_DV)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].rol").value(hasItem(DEFAULT_ROL)))
            .andExpect(jsonPath("$.[*].rut").value(hasItem(DEFAULT_RUT)))
            .andExpect(jsonPath("$.[*].nombreComercial").value(hasItem(DEFAULT_NOMBRE_COMERCIAL)))
            .andExpect(jsonPath("$.[*].nit").value(hasItem(DEFAULT_NIT)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].indicativo").value(hasItem(DEFAULT_INDICATIVO)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].genero").value(hasItem(DEFAULT_GENERO)))
            .andExpect(jsonPath("$.[*].edad").value(hasItem(DEFAULT_EDAD.toString())))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(DEFAULT_FOTO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.intValue())))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @Test
    @Transactional
    void getUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get the usuario
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL_ID, usuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(usuario.getId().intValue()))
            .andExpect(jsonPath("$.nombres").value(DEFAULT_NOMBRES))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS))
            .andExpect(jsonPath("$.tipoPersona").value(DEFAULT_TIPO_PERSONA))
            .andExpect(jsonPath("$.tipoDocumento").value(DEFAULT_TIPO_DOCUMENTO))
            .andExpect(jsonPath("$.documento").value(DEFAULT_DOCUMENTO))
            .andExpect(jsonPath("$.dv").value(DEFAULT_DV))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.rol").value(DEFAULT_ROL))
            .andExpect(jsonPath("$.rut").value(DEFAULT_RUT))
            .andExpect(jsonPath("$.nombreComercial").value(DEFAULT_NOMBRE_COMERCIAL))
            .andExpect(jsonPath("$.nit").value(DEFAULT_NIT))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.indicativo").value(DEFAULT_INDICATIVO))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.genero").value(DEFAULT_GENERO))
            .andExpect(jsonPath("$.edad").value(DEFAULT_EDAD.toString()))
            .andExpect(jsonPath("$.foto").value(DEFAULT_FOTO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.intValue()))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUsuario() throws Exception {
        // Get the usuario
        restUsuarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario
        Usuario updatedUsuario = usuarioRepository.findById(usuario.getId()).get();
        // Disconnect from session so that the updates on updatedUsuario are not directly saved in db
        em.detach(updatedUsuario);
        updatedUsuario
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .tipoPersona(UPDATED_TIPO_PERSONA)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .documento(UPDATED_DOCUMENTO)
            .dv(UPDATED_DV)
            .estado(UPDATED_ESTADO)
            .rol(UPDATED_ROL)
            .rut(UPDATED_RUT)
            .nombreComercial(UPDATED_NOMBRE_COMERCIAL)
            .nit(UPDATED_NIT)
            .direccion(UPDATED_DIRECCION)
            .indicativo(UPDATED_INDICATIVO)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .genero(UPDATED_GENERO)
            .edad(UPDATED_EDAD)
            .foto(UPDATED_FOTO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUsuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testUsuario.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testUsuario.getTipoPersona()).isEqualTo(UPDATED_TIPO_PERSONA);
        assertThat(testUsuario.getTipoDocumento()).isEqualTo(UPDATED_TIPO_DOCUMENTO);
        assertThat(testUsuario.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
        assertThat(testUsuario.getDv()).isEqualTo(UPDATED_DV);
        assertThat(testUsuario.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testUsuario.getRol()).isEqualTo(UPDATED_ROL);
        assertThat(testUsuario.getRut()).isEqualTo(UPDATED_RUT);
        assertThat(testUsuario.getNombreComercial()).isEqualTo(UPDATED_NOMBRE_COMERCIAL);
        assertThat(testUsuario.getNit()).isEqualTo(UPDATED_NIT);
        assertThat(testUsuario.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testUsuario.getIndicativo()).isEqualTo(UPDATED_INDICATIVO);
        assertThat(testUsuario.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testUsuario.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsuario.getGenero()).isEqualTo(UPDATED_GENERO);
        assertThat(testUsuario.getEdad()).isEqualTo(UPDATED_EDAD);
        assertThat(testUsuario.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testUsuario.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testUsuario.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .documento(UPDATED_DOCUMENTO)
            .rol(UPDATED_ROL)
            .rut(UPDATED_RUT)
            .nombreComercial(UPDATED_NOMBRE_COMERCIAL)
            .direccion(UPDATED_DIRECCION)
            .indicativo(UPDATED_INDICATIVO)
            .telefono(UPDATED_TELEFONO)
            .genero(UPDATED_GENERO)
            .edad(UPDATED_EDAD);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testUsuario.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testUsuario.getTipoPersona()).isEqualTo(DEFAULT_TIPO_PERSONA);
        assertThat(testUsuario.getTipoDocumento()).isEqualTo(DEFAULT_TIPO_DOCUMENTO);
        assertThat(testUsuario.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
        assertThat(testUsuario.getDv()).isEqualTo(DEFAULT_DV);
        assertThat(testUsuario.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testUsuario.getRol()).isEqualTo(UPDATED_ROL);
        assertThat(testUsuario.getRut()).isEqualTo(UPDATED_RUT);
        assertThat(testUsuario.getNombreComercial()).isEqualTo(UPDATED_NOMBRE_COMERCIAL);
        assertThat(testUsuario.getNit()).isEqualTo(DEFAULT_NIT);
        assertThat(testUsuario.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testUsuario.getIndicativo()).isEqualTo(UPDATED_INDICATIVO);
        assertThat(testUsuario.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testUsuario.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUsuario.getGenero()).isEqualTo(UPDATED_GENERO);
        assertThat(testUsuario.getEdad()).isEqualTo(UPDATED_EDAD);
        assertThat(testUsuario.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testUsuario.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testUsuario.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .tipoPersona(UPDATED_TIPO_PERSONA)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .documento(UPDATED_DOCUMENTO)
            .dv(UPDATED_DV)
            .estado(UPDATED_ESTADO)
            .rol(UPDATED_ROL)
            .rut(UPDATED_RUT)
            .nombreComercial(UPDATED_NOMBRE_COMERCIAL)
            .nit(UPDATED_NIT)
            .direccion(UPDATED_DIRECCION)
            .indicativo(UPDATED_INDICATIVO)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .genero(UPDATED_GENERO)
            .edad(UPDATED_EDAD)
            .foto(UPDATED_FOTO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testUsuario.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testUsuario.getTipoPersona()).isEqualTo(UPDATED_TIPO_PERSONA);
        assertThat(testUsuario.getTipoDocumento()).isEqualTo(UPDATED_TIPO_DOCUMENTO);
        assertThat(testUsuario.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
        assertThat(testUsuario.getDv()).isEqualTo(UPDATED_DV);
        assertThat(testUsuario.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testUsuario.getRol()).isEqualTo(UPDATED_ROL);
        assertThat(testUsuario.getRut()).isEqualTo(UPDATED_RUT);
        assertThat(testUsuario.getNombreComercial()).isEqualTo(UPDATED_NOMBRE_COMERCIAL);
        assertThat(testUsuario.getNit()).isEqualTo(UPDATED_NIT);
        assertThat(testUsuario.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testUsuario.getIndicativo()).isEqualTo(UPDATED_INDICATIVO);
        assertThat(testUsuario.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testUsuario.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsuario.getGenero()).isEqualTo(UPDATED_GENERO);
        assertThat(testUsuario.getEdad()).isEqualTo(UPDATED_EDAD);
        assertThat(testUsuario.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testUsuario.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testUsuario.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(usuario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeDelete = usuarioRepository.findAll().size();

        // Delete the usuario
        restUsuarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, usuario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
