package com.ivansantander.service;

import com.ivansantander.domain.NotaContable;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link NotaContable}.
 */
public interface NotaContableService {
    /**
     * Save a notaContable.
     *
     * @param notaContable the entity to save.
     * @return the persisted entity.
     */
    NotaContable save(NotaContable notaContable);

    /**
     * Partially updates a notaContable.
     *
     * @param notaContable the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NotaContable> partialUpdate(NotaContable notaContable);

    /**
     * Get all the notaContables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NotaContable> findAll(Pageable pageable);

    /**
     * Get the "id" notaContable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NotaContable> findOne(Long id);

    /**
     * Delete the "id" notaContable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
