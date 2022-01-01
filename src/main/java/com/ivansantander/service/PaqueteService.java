package com.ivansantander.service;

import com.ivansantander.domain.Paquete;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Paquete}.
 */
public interface PaqueteService {
    /**
     * Save a paquete.
     *
     * @param paquete the entity to save.
     * @return the persisted entity.
     */
    Paquete save(Paquete paquete);

    /**
     * Partially updates a paquete.
     *
     * @param paquete the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Paquete> partialUpdate(Paquete paquete);

    /**
     * Get all the paquetes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Paquete> findAll(Pageable pageable);

    /**
     * Get the "id" paquete.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Paquete> findOne(Long id);

    /**
     * Delete the "id" paquete.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
