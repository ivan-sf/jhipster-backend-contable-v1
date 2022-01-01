package com.ivansantander.service;

import com.ivansantander.domain.DetalleFactura;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link DetalleFactura}.
 */
public interface DetalleFacturaService {
    /**
     * Save a detalleFactura.
     *
     * @param detalleFactura the entity to save.
     * @return the persisted entity.
     */
    DetalleFactura save(DetalleFactura detalleFactura);

    /**
     * Partially updates a detalleFactura.
     *
     * @param detalleFactura the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DetalleFactura> partialUpdate(DetalleFactura detalleFactura);

    /**
     * Get all the detalleFacturas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DetalleFactura> findAll(Pageable pageable);

    /**
     * Get the "id" detalleFactura.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DetalleFactura> findOne(Long id);

    /**
     * Delete the "id" detalleFactura.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
