package com.ivansantander.service;

import com.ivansantander.domain.Regimen;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Regimen}.
 */
public interface RegimenService {
    /**
     * Save a regimen.
     *
     * @param regimen the entity to save.
     * @return the persisted entity.
     */
    Regimen save(Regimen regimen);

    /**
     * Partially updates a regimen.
     *
     * @param regimen the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Regimen> partialUpdate(Regimen regimen);

    /**
     * Get all the regimen.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Regimen> findAll(Pageable pageable);

    /**
     * Get the "id" regimen.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Regimen> findOne(Long id);

    /**
     * Delete the "id" regimen.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
