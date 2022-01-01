package com.ivansantander.service.impl;

import com.ivansantander.domain.TipoObjeto;
import com.ivansantander.repository.TipoObjetoRepository;
import com.ivansantander.service.TipoObjetoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TipoObjeto}.
 */
@Service
@Transactional
public class TipoObjetoServiceImpl implements TipoObjetoService {

    private final Logger log = LoggerFactory.getLogger(TipoObjetoServiceImpl.class);

    private final TipoObjetoRepository tipoObjetoRepository;

    public TipoObjetoServiceImpl(TipoObjetoRepository tipoObjetoRepository) {
        this.tipoObjetoRepository = tipoObjetoRepository;
    }

    @Override
    public TipoObjeto save(TipoObjeto tipoObjeto) {
        log.debug("Request to save TipoObjeto : {}", tipoObjeto);
        return tipoObjetoRepository.save(tipoObjeto);
    }

    @Override
    public Optional<TipoObjeto> partialUpdate(TipoObjeto tipoObjeto) {
        log.debug("Request to partially update TipoObjeto : {}", tipoObjeto);

        return tipoObjetoRepository
            .findById(tipoObjeto.getId())
            .map(existingTipoObjeto -> {
                if (tipoObjeto.getNombre() != null) {
                    existingTipoObjeto.setNombre(tipoObjeto.getNombre());
                }
                if (tipoObjeto.getFechaRegistro() != null) {
                    existingTipoObjeto.setFechaRegistro(tipoObjeto.getFechaRegistro());
                }
                if (tipoObjeto.getCodigoDian() != null) {
                    existingTipoObjeto.setCodigoDian(tipoObjeto.getCodigoDian());
                }

                return existingTipoObjeto;
            })
            .map(tipoObjetoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoObjeto> findAll(Pageable pageable) {
        log.debug("Request to get all TipoObjetos");
        return tipoObjetoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoObjeto> findOne(Long id) {
        log.debug("Request to get TipoObjeto : {}", id);
        return tipoObjetoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoObjeto : {}", id);
        tipoObjetoRepository.deleteById(id);
    }
}
