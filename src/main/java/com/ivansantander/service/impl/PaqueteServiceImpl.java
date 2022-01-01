package com.ivansantander.service.impl;

import com.ivansantander.domain.Paquete;
import com.ivansantander.repository.PaqueteRepository;
import com.ivansantander.service.PaqueteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Paquete}.
 */
@Service
@Transactional
public class PaqueteServiceImpl implements PaqueteService {

    private final Logger log = LoggerFactory.getLogger(PaqueteServiceImpl.class);

    private final PaqueteRepository paqueteRepository;

    public PaqueteServiceImpl(PaqueteRepository paqueteRepository) {
        this.paqueteRepository = paqueteRepository;
    }

    @Override
    public Paquete save(Paquete paquete) {
        log.debug("Request to save Paquete : {}", paquete);
        return paqueteRepository.save(paquete);
    }

    @Override
    public Optional<Paquete> partialUpdate(Paquete paquete) {
        log.debug("Request to partially update Paquete : {}", paquete);

        return paqueteRepository
            .findById(paquete.getId())
            .map(existingPaquete -> {
                if (paquete.getCantidad() != null) {
                    existingPaquete.setCantidad(paquete.getCantidad());
                }

                return existingPaquete;
            })
            .map(paqueteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Paquete> findAll(Pageable pageable) {
        log.debug("Request to get all Paquetes");
        return paqueteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Paquete> findOne(Long id) {
        log.debug("Request to get Paquete : {}", id);
        return paqueteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Paquete : {}", id);
        paqueteRepository.deleteById(id);
    }
}
