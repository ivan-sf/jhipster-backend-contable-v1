package com.ivansantander.service;

import com.ivansantander.domain.Vencimiento;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Vencimiento}.
 */
public interface VencimientoService {
    /**
     * Save a vencimiento.
     *
     * @param vencimiento the entity to save.
     * @return the persisted entity.
     */
    Vencimiento save(Vencimiento vencimiento);

    /**
     * Partially updates a vencimiento.
     *
     * @param vencimiento the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Vencimiento> partialUpdate(Vencimiento vencimiento);

    /**
     * Get all the vencimientos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Vencimiento> findAll(Pageable pageable);

    /**
     * Get the "id" vencimiento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Vencimiento> findOne(Long id);

    /**
     * Delete the "id" vencimiento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
