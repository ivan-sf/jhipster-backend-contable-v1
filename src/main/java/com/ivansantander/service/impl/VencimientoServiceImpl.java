package com.ivansantander.service.impl;

import com.ivansantander.domain.Vencimiento;
import com.ivansantander.repository.VencimientoRepository;
import com.ivansantander.service.VencimientoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vencimiento}.
 */
@Service
@Transactional
public class VencimientoServiceImpl implements VencimientoService {

    private final Logger log = LoggerFactory.getLogger(VencimientoServiceImpl.class);

    private final VencimientoRepository vencimientoRepository;

    public VencimientoServiceImpl(VencimientoRepository vencimientoRepository) {
        this.vencimientoRepository = vencimientoRepository;
    }

    @Override
    public Vencimiento save(Vencimiento vencimiento) {
        log.debug("Request to save Vencimiento : {}", vencimiento);
        return vencimientoRepository.save(vencimiento);
    }

    @Override
    public Optional<Vencimiento> partialUpdate(Vencimiento vencimiento) {
        log.debug("Request to partially update Vencimiento : {}", vencimiento);

        return vencimientoRepository
            .findById(vencimiento.getId())
            .map(existingVencimiento -> {
                if (vencimiento.getFecha() != null) {
                    existingVencimiento.setFecha(vencimiento.getFecha());
                }

                return existingVencimiento;
            })
            .map(vencimientoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Vencimiento> findAll(Pageable pageable) {
        log.debug("Request to get all Vencimientos");
        return vencimientoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vencimiento> findOne(Long id) {
        log.debug("Request to get Vencimiento : {}", id);
        return vencimientoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vencimiento : {}", id);
        vencimientoRepository.deleteById(id);
    }
}
