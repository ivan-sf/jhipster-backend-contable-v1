package com.ivansantander.service;

import com.ivansantander.domain.Factura;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Factura}.
 */
public interface FacturaService {
    /**
     * Save a factura.
     *
     * @param factura the entity to save.
     * @return the persisted entity.
     */
    Factura save(Factura factura);

    /**
     * Partially updates a factura.
     *
     * @param factura the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Factura> partialUpdate(Factura factura);

    /**
     * Get all the facturas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Factura> findAll(Pageable pageable);

    /**
     * Get the "id" factura.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Factura> findOne(Long id);

    /**
     * Delete the "id" factura.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
