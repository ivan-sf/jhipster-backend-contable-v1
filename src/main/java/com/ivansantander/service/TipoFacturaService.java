package com.ivansantander.service;

import com.ivansantander.domain.TipoFactura;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TipoFactura}.
 */
public interface TipoFacturaService {
    /**
     * Save a tipoFactura.
     *
     * @param tipoFactura the entity to save.
     * @return the persisted entity.
     */
    TipoFactura save(TipoFactura tipoFactura);

    /**
     * Partially updates a tipoFactura.
     *
     * @param tipoFactura the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TipoFactura> partialUpdate(TipoFactura tipoFactura);

    /**
     * Get all the tipoFacturas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TipoFactura> findAll(Pageable pageable);

    /**
     * Get the "id" tipoFactura.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TipoFactura> findOne(Long id);

    /**
     * Delete the "id" tipoFactura.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
