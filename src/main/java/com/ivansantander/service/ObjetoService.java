package com.ivansantander.service;

import com.ivansantander.domain.Objeto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Objeto}.
 */
public interface ObjetoService {
    /**
     * Save a objeto.
     *
     * @param objeto the entity to save.
     * @return the persisted entity.
     */
    Objeto save(Objeto objeto);

    /**
     * Partially updates a objeto.
     *
     * @param objeto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Objeto> partialUpdate(Objeto objeto);

    /**
     * Get all the objetos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Objeto> findAll(Pageable pageable);

    /**
     * Get the "id" objeto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Objeto> findOne(Long id);

    /**
     * Delete the "id" objeto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
