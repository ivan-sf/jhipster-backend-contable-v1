package com.ivansantander.service.impl;

import com.ivansantander.domain.DetallesObjeto;
import com.ivansantander.repository.DetallesObjetoRepository;
import com.ivansantander.service.DetallesObjetoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DetallesObjeto}.
 */
@Service
@Transactional
public class DetallesObjetoServiceImpl implements DetallesObjetoService {

    private final Logger log = LoggerFactory.getLogger(DetallesObjetoServiceImpl.class);

    private final DetallesObjetoRepository detallesObjetoRepository;

    public DetallesObjetoServiceImpl(DetallesObjetoRepository detallesObjetoRepository) {
        this.detallesObjetoRepository = detallesObjetoRepository;
    }

    @Override
    public DetallesObjeto save(DetallesObjeto detallesObjeto) {
        log.debug("Request to save DetallesObjeto : {}", detallesObjeto);
        return detallesObjetoRepository.save(detallesObjeto);
    }

    @Override
    public Optional<DetallesObjeto> partialUpdate(DetallesObjeto detallesObjeto) {
        log.debug("Request to partially update DetallesObjeto : {}", detallesObjeto);

        return detallesObjetoRepository
            .findById(detallesObjeto.getId())
            .map(existingDetallesObjeto -> {
                if (detallesObjeto.getNombre() != null) {
                    existingDetallesObjeto.setNombre(detallesObjeto.getNombre());
                }
                if (detallesObjeto.getDescripcion() != null) {
                    existingDetallesObjeto.setDescripcion(detallesObjeto.getDescripcion());
                }
                if (detallesObjeto.getFechaRegistro() != null) {
                    existingDetallesObjeto.setFechaRegistro(detallesObjeto.getFechaRegistro());
                }

                return existingDetallesObjeto;
            })
            .map(detallesObjetoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DetallesObjeto> findAll(Pageable pageable) {
        log.debug("Request to get all DetallesObjetos");
        return detallesObjetoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetallesObjeto> findOne(Long id) {
        log.debug("Request to get DetallesObjeto : {}", id);
        return detallesObjetoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DetallesObjeto : {}", id);
        detallesObjetoRepository.deleteById(id);
    }
}
