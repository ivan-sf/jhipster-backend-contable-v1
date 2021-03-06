package com.ivansantander.service.impl;

import com.ivansantander.domain.Codigo;
import com.ivansantander.repository.CodigoRepository;
import com.ivansantander.service.CodigoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Codigo}.
 */
@Service
@Transactional
public class CodigoServiceImpl implements CodigoService {

    private final Logger log = LoggerFactory.getLogger(CodigoServiceImpl.class);

    private final CodigoRepository codigoRepository;

    public CodigoServiceImpl(CodigoRepository codigoRepository) {
        this.codigoRepository = codigoRepository;
    }

    @Override
    public Codigo save(Codigo codigo) {
        log.debug("Request to save Codigo : {}", codigo);
        return codigoRepository.save(codigo);
    }

    @Override
    public Optional<Codigo> partialUpdate(Codigo codigo) {
        log.debug("Request to partially update Codigo : {}", codigo);

        return codigoRepository
            .findById(codigo.getId())
            .map(existingCodigo -> {
                if (codigo.getBarCode() != null) {
                    existingCodigo.setBarCode(codigo.getBarCode());
                }
                if (codigo.getQrCode() != null) {
                    existingCodigo.setQrCode(codigo.getQrCode());
                }
                if (codigo.getCantidad() != null) {
                    existingCodigo.setCantidad(codigo.getCantidad());
                }
                if (codigo.getFechaRegistro() != null) {
                    existingCodigo.setFechaRegistro(codigo.getFechaRegistro());
                }

                return existingCodigo;
            })
            .map(codigoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Codigo> findAll(Pageable pageable) {
        log.debug("Request to get all Codigos");
        return codigoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Codigo> findOne(Long id) {
        log.debug("Request to get Codigo : {}", id);
        return codigoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Codigo : {}", id);
        codigoRepository.deleteById(id);
    }
}
