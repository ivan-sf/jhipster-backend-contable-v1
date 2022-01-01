package com.ivansantander.service;

import com.ivansantander.domain.TipoObjeto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TipoObjeto}.
 */
public interface TipoObjetoService {
    /**
     * Save a tipoObjeto.
     *
     * @param tipoObjeto the entity to save.
     * @return the persisted entity.
     */
    TipoObjeto save(TipoObjeto tipoObjeto);

    /**
     * Partially updates a tipoObjeto.
     *
     * @param tipoObjeto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TipoObjeto> partialUpdate(TipoObjeto tipoObjeto);

    /**
     * Get all the tipoObjetos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TipoObjeto> findAll(Pageable pageable);

    /**
     * Get the "id" tipoObjeto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TipoObjeto> findOne(Long id);

    /**
     * Delete the "id" tipoObjeto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
