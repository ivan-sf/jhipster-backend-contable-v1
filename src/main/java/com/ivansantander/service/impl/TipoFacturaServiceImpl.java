package com.ivansantander.service.impl;

import com.ivansantander.domain.TipoFactura;
import com.ivansantander.repository.TipoFacturaRepository;
import com.ivansantander.service.TipoFacturaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TipoFactura}.
 */
@Service
@Transactional
public class TipoFacturaServiceImpl implements TipoFacturaService {

    private final Logger log = LoggerFactory.getLogger(TipoFacturaServiceImpl.class);

    private final TipoFacturaRepository tipoFacturaRepository;

    public TipoFacturaServiceImpl(TipoFacturaRepository tipoFacturaRepository) {
        this.tipoFacturaRepository = tipoFacturaRepository;
    }

    @Override
    public TipoFactura save(TipoFactura tipoFactura) {
        log.debug("Request to save TipoFactura : {}", tipoFactura);
        return tipoFacturaRepository.save(tipoFactura);
    }

    @Override
    public Optional<TipoFactura> partialUpdate(TipoFactura tipoFactura) {
        log.debug("Request to partially update TipoFactura : {}", tipoFactura);

        return tipoFacturaRepository
            .findById(tipoFactura.getId())
            .map(existingTipoFactura -> {
                if (tipoFactura.getNombre() != null) {
                    existingTipoFactura.setNombre(tipoFactura.getNombre());
                }
                if (tipoFactura.getPrefijoInicial() != null) {
                    existingTipoFactura.setPrefijoInicial(tipoFactura.getPrefijoInicial());
                }
                if (tipoFactura.getPrefijoFinal() != null) {
                    existingTipoFactura.setPrefijoFinal(tipoFactura.getPrefijoFinal());
                }
                if (tipoFactura.getPrefijoActual() != null) {
                    existingTipoFactura.setPrefijoActual(tipoFactura.getPrefijoActual());
                }

                return existingTipoFactura;
            })
            .map(tipoFacturaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoFactura> findAll(Pageable pageable) {
        log.debug("Request to get all TipoFacturas");
        return tipoFacturaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoFactura> findOne(Long id) {
        log.debug("Request to get TipoFactura : {}", id);
        return tipoFacturaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoFactura : {}", id);
        tipoFacturaRepository.deleteById(id);
    }
}
