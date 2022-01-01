package com.ivansantander.service;

import com.ivansantander.domain.DetallesObjeto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link DetallesObjeto}.
 */
public interface DetallesObjetoService {
    /**
     * Save a detallesObjeto.
     *
     * @param detallesObjeto the entity to save.
     * @return the persisted entity.
     */
    DetallesObjeto save(DetallesObjeto detallesObjeto);

    /**
     * Partially updates a detallesObjeto.
     *
     * @param detallesObjeto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DetallesObjeto> partialUpdate(DetallesObjeto detallesObjeto);

    /**
     * Get all the detallesObjetos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DetallesObjeto> findAll(Pageable pageable);

    /**
     * Get the "id" detallesObjeto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DetallesObjeto> findOne(Long id);

    /**
     * Delete the "id" detallesObjeto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
