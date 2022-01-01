package com.ivansantander.service.impl;

import com.ivansantander.domain.Trabajo;
import com.ivansantander.repository.TrabajoRepository;
import com.ivansantander.service.TrabajoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Trabajo}.
 */
@Service
@Transactional
public class TrabajoServiceImpl implements TrabajoService {

    private final Logger log = LoggerFactory.getLogger(TrabajoServiceImpl.class);

    private final TrabajoRepository trabajoRepository;

    public TrabajoServiceImpl(TrabajoRepository trabajoRepository) {
        this.trabajoRepository = trabajoRepository;
    }

    @Override
    public Trabajo save(Trabajo trabajo) {
        log.debug("Request to save Trabajo : {}", trabajo);
        return trabajoRepository.save(trabajo);
    }

    @Override
    public Optional<Trabajo> partialUpdate(Trabajo trabajo) {
        log.debug("Request to partially update Trabajo : {}", trabajo);

        return trabajoRepository
            .findById(trabajo.getId())
            .map(existingTrabajo -> {
                if (trabajo.getNombre() != null) {
                    existingTrabajo.setNombre(trabajo.getNombre());
                }
                if (trabajo.getCargo() != null) {
                    existingTrabajo.setCargo(trabajo.getCargo());
                }
                if (trabajo.getSalario() != null) {
                    existingTrabajo.setSalario(trabajo.getSalario());
                }
                if (trabajo.getSalud() != null) {
                    existingTrabajo.setSalud(trabajo.getSalud());
                }
                if (trabajo.getPension() != null) {
                    existingTrabajo.setPension(trabajo.getPension());
                }
                if (trabajo.getObservacon() != null) {
                    existingTrabajo.setObservacon(trabajo.getObservacon());
                }
                if (trabajo.getFechaRegistro() != null) {
                    existingTrabajo.setFechaRegistro(trabajo.getFechaRegistro());
                }

                return existingTrabajo;
            })
            .map(trabajoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Trabajo> findAll(Pageable pageable) {
        log.debug("Request to get all Trabajos");
        return trabajoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trabajo> findOne(Long id) {
        log.debug("Request to get Trabajo : {}", id);
        return trabajoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Trabajo : {}", id);
        trabajoRepository.deleteById(id);
    }
}
