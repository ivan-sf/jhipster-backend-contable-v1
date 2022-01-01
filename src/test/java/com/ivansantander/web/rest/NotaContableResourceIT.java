package com.ivansantander.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ivansantander.IntegrationTest;
import com.ivansantander.domain.NotaContable;
import com.ivansantander.repository.NotaContableRepository;
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
 * Integration tests for the {@link NotaContableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotaContableResourceIT {

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    private static final String ENTITY_API_URL = "/api/nota-contables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NotaContableRepository notaContableRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotaContableMockMvc;

    private NotaContable notaContable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotaContable createEntity(EntityManager em) {
        NotaContable notaContable = new NotaContable().numero(DEFAULT_NUMERO);
        return notaContable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotaContable createUpdatedEntity(EntityManager em) {
        NotaContable notaContable = new NotaContable().numero(UPDATED_NUMERO);
        return notaContable;
    }

    @BeforeEach
    public void initTest() {
        notaContable = createEntity(em);
    }

    @Test
    @Transactional
    void createNotaContable() throws Exception {
        int databaseSizeBeforeCreate = notaContableRepository.findAll().size();
        // Create the NotaContable
        restNotaContableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notaContable)))
            .andExpect(status().isCreated());

        // Validate the NotaContable in the database
        List<NotaContable> notaContableList = notaContableRepository.findAll();
        assertThat(notaContableList).hasSize(databaseSizeBeforeCreate + 1);
        NotaContable testNotaContable = notaContableList.get(notaContableList.size() - 1);
        assertThat(testNotaContable.getNumero()).isEqualTo(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    void createNotaContableWithExistingId() throws Exception {
        // Create the NotaContable with an existing ID
        notaContable.setId(1L);

        int databaseSizeBeforeCreate = notaContableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotaContableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notaContable)))
            .andExpect(status().isBadRequest());

        // Validate the NotaContable in the database
        List<NotaContable> notaContableList = notaContableRepository.findAll();
        assertThat(notaContableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNotaContables() throws Exception {
        // Initialize the database
        notaContableRepository.saveAndFlush(notaContable);

        // Get all the notaContableList
        restNotaContableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notaContable.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)));
    }

    @Test
    @Transactional
    void getNotaContable() throws Exception {
        // Initialize the database
        notaContableRepository.saveAndFlush(notaContable);

        // Get the notaContable
        restNotaContableMockMvc
            .perform(get(ENTITY_API_URL_ID, notaContable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notaContable.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO));
    }

    @Test
    @Transactional
    void getNonExistingNotaContable() throws Exception {
        // Get the notaContable
        restNotaContableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNotaContable() throws Exception {
        // Initialize the database
        notaContableRepository.saveAndFlush(notaContable);

        int databaseSizeBeforeUpdate = notaContableRepository.findAll().size();

        // Update the notaContable
        NotaContable updatedNotaContable = notaContableRepository.findById(notaContable.getId()).get();
        // Disconnect from session so that the updates on updatedNotaContable are not directly saved in db
        em.detach(updatedNotaContable);
        updatedNotaContable.numero(UPDATED_NUMERO);

        restNotaContableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNotaContable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNotaContable))
            )
            .andExpect(status().isOk());

        // Validate the NotaContable in the database
        List<NotaContable> notaContableList = notaContableRepository.findAll();
        assertThat(notaContableList).hasSize(databaseSizeBeforeUpdate);
        NotaContable testNotaContable = notaContableList.get(notaContableList.size() - 1);
        assertThat(testNotaContable.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void putNonExistingNotaContable() throws Exception {
        int databaseSizeBeforeUpdate = notaContableRepository.findAll().size();
        notaContable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotaContableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notaContable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notaContable))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotaContable in the database
        List<NotaContable> notaContableList = notaContableRepository.findAll();
        assertThat(notaContableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotaContable() throws Exception {
        int databaseSizeBeforeUpdate = notaContableRepository.findAll().size();
        notaContable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotaContableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notaContable))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotaContable in the database
        List<NotaContable> notaContableList = notaContableRepository.findAll();
        assertThat(notaContableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotaContable() throws Exception {
        int databaseSizeBeforeUpdate = notaContableRepository.findAll().size();
        notaContable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotaContableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notaContable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotaContable in the database
        List<NotaContable> notaContableList = notaContableRepository.findAll();
        assertThat(notaContableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotaContableWithPatch() throws Exception {
        // Initialize the database
        notaContableRepository.saveAndFlush(notaContable);

        int databaseSizeBeforeUpdate = notaContableRepository.findAll().size();

        // Update the notaContable using partial update
        NotaContable partialUpdatedNotaContable = new NotaContable();
        partialUpdatedNotaContable.setId(notaContable.getId());

        partialUpdatedNotaContable.numero(UPDATED_NUMERO);

        restNotaContableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotaContable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotaContable))
            )
            .andExpect(status().isOk());

        // Validate the NotaContable in the database
        List<NotaContable> notaContableList = notaContableRepository.findAll();
        assertThat(notaContableList).hasSize(databaseSizeBeforeUpdate);
        NotaContable testNotaContable = notaContableList.get(notaContableList.size() - 1);
        assertThat(testNotaContable.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void fullUpdateNotaContableWithPatch() throws Exception {
        // Initialize the database
        notaContableRepository.saveAndFlush(notaContable);

        int databaseSizeBeforeUpdate = notaContableRepository.findAll().size();

        // Update the notaContable using partial update
        NotaContable partialUpdatedNotaContable = new NotaContable();
        partialUpdatedNotaContable.setId(notaContable.getId());

        partialUpdatedNotaContable.numero(UPDATED_NUMERO);

        restNotaContableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotaContable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotaContable))
            )
            .andExpect(status().isOk());

        // Validate the NotaContable in the database
        List<NotaContable> notaContableList = notaContableRepository.findAll();
        assertThat(notaContableList).hasSize(databaseSizeBeforeUpdate);
        NotaContable testNotaContable = notaContableList.get(notaContableList.size() - 1);
        assertThat(testNotaContable.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void patchNonExistingNotaContable() throws Exception {
        int databaseSizeBeforeUpdate = notaContableRepository.findAll().size();
        notaContable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotaContableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notaContable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notaContable))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotaContable in the database
        List<NotaContable> notaContableList = notaContableRepository.findAll();
        assertThat(notaContableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotaContable() throws Exception {
        int databaseSizeBeforeUpdate = notaContableRepository.findAll().size();
        notaContable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotaContableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notaContable))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotaContable in the database
        List<NotaContable> notaContableList = notaContableRepository.findAll();
        assertThat(notaContableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotaContable() throws Exception {
        int databaseSizeBeforeUpdate = notaContableRepository.findAll().size();
        notaContable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotaContableMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(notaContable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotaContable in the database
        List<NotaContable> notaContableList = notaContableRepository.findAll();
        assertThat(notaContableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotaContable() throws Exception {
        // Initialize the database
        notaContableRepository.saveAndFlush(notaContable);

        int databaseSizeBeforeDelete = notaContableRepository.findAll().size();

        // Delete the notaContable
        restNotaContableMockMvc
            .perform(delete(ENTITY_API_URL_ID, notaContable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NotaContable> notaContableList = notaContableRepository.findAll();
        assertThat(notaContableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
