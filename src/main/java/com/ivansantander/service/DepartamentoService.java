package com.ivansantander.service;

import com.ivansantander.domain.Departamento;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Departamento}.
 */
public interface DepartamentoService {
    /**
     * Save a departamento.
     *
     * @param departamento the entity to save.
     * @return the persisted entity.
     */
    Departamento save(Departamento departamento);

    /**
     * Partially updates a departamento.
     *
     * @param departamento the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Departamento> partialUpdate(Departamento departamento);

    /**
     * Get all the departamentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Departamento> findAll(Pageable pageable);

    /**
     * Get the "id" departamento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Departamento> findOne(Long id);

    /**
     * Delete the "id" departamento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
