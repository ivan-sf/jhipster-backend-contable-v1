package com.ivansantander.service;

import com.ivansantander.domain.Lote;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Lote}.
 */
public interface LoteService {
    /**
     * Save a lote.
     *
     * @param lote the entity to save.
     * @return the persisted entity.
     */
    Lote save(Lote lote);

    /**
     * Partially updates a lote.
     *
     * @param lote the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Lote> partialUpdate(Lote lote);

    /**
     * Get all the lotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Lote> findAll(Pageable pageable);

    /**
     * Get the "id" lote.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Lote> findOne(Long id);

    /**
     * Delete the "id" lote.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
