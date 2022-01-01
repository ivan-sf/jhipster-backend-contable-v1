package com.ivansantander.service.impl;

import com.ivansantander.domain.Regimen;
import com.ivansantander.repository.RegimenRepository;
import com.ivansantander.service.RegimenService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Regimen}.
 */
@Service
@Transactional
public class RegimenServiceImpl implements RegimenService {

    private final Logger log = LoggerFactory.getLogger(RegimenServiceImpl.class);

    private final RegimenRepository regimenRepository;

    public RegimenServiceImpl(RegimenRepository regimenRepository) {
        this.regimenRepository = regimenRepository;
    }

    @Override
    public Regimen save(Regimen regimen) {
        log.debug("Request to save Regimen : {}", regimen);
        return regimenRepository.save(regimen);
    }

    @Override
    public Optional<Regimen> partialUpdate(Regimen regimen) {
        log.debug("Request to partially update Regimen : {}", regimen);

        return regimenRepository
            .findById(regimen.getId())
            .map(existingRegimen -> {
                if (regimen.getTipoRegimen() != null) {
                    existingRegimen.setTipoRegimen(regimen.getTipoRegimen());
                }
                if (regimen.getNombreRegimen() != null) {
                    existingRegimen.setNombreRegimen(regimen.getNombreRegimen());
                }
                if (regimen.getFechaRegistro() != null) {
                    existingRegimen.setFechaRegistro(regimen.getFechaRegistro());
                }

                return existingRegimen;
            })
            .map(regimenRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Regimen> findAll(Pageable pageable) {
        log.debug("Request to get all Regimen");
        return regimenRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Regimen> findOne(Long id) {
        log.debug("Request to get Regimen : {}", id);
        return regimenRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Regimen : {}", id);
        regimenRepository.deleteById(id);
    }
}
