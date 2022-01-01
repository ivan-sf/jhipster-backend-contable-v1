package com.ivansantander.service;

import com.ivansantander.domain.Municipio;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Municipio}.
 */
public interface MunicipioService {
    /**
     * Save a municipio.
     *
     * @param municipio the entity to save.
     * @return the persisted entity.
     */
    Municipio save(Municipio municipio);

    /**
     * Partially updates a municipio.
     *
     * @param municipio the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Municipio> partialUpdate(Municipio municipio);

    /**
     * Get all the municipios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Municipio> findAll(Pageable pageable);

    /**
     * Get the "id" municipio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Municipio> findOne(Long id);

    /**
     * Delete the "id" municipio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
