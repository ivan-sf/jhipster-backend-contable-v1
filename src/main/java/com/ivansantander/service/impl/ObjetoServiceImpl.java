package com.ivansantander.service.impl;

import com.ivansantander.domain.Objeto;
import com.ivansantander.repository.ObjetoRepository;
import com.ivansantander.service.ObjetoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Objeto}.
 */
@Service
@Transactional
public class ObjetoServiceImpl implements ObjetoService {

    private final Logger log = LoggerFactory.getLogger(ObjetoServiceImpl.class);

    private final ObjetoRepository objetoRepository;

    public ObjetoServiceImpl(ObjetoRepository objetoRepository) {
        this.objetoRepository = objetoRepository;
    }

    @Override
    public Objeto save(Objeto objeto) {
        log.debug("Request to save Objeto : {}", objeto);
        return objetoRepository.save(objeto);
    }

    @Override
    public Optional<Objeto> partialUpdate(Objeto objeto) {
        log.debug("Request to partially update Objeto : {}", objeto);

        return objetoRepository
            .findById(objeto.getId())
            .map(existingObjeto -> {
                if (objeto.getNombre() != null) {
                    existingObjeto.setNombre(objeto.getNombre());
                }
                if (objeto.getCantidad() != null) {
                    existingObjeto.setCantidad(objeto.getCantidad());
                }
                if (objeto.getCodigoDian() != null) {
                    existingObjeto.setCodigoDian(objeto.getCodigoDian());
                }
                if (objeto.getDetalleObjeto() != null) {
                    existingObjeto.setDetalleObjeto(objeto.getDetalleObjeto());
                }
                if (objeto.getVencimiento() != null) {
                    existingObjeto.setVencimiento(objeto.getVencimiento());
                }
                if (objeto.getFechaRegistro() != null) {
                    existingObjeto.setFechaRegistro(objeto.getFechaRegistro());
                }

                return existingObjeto;
            })
            .map(objetoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Objeto> findAll(Pageable pageable) {
        log.debug("Request to get all Objetos");
        return objetoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Objeto> findOne(Long id) {
        log.debug("Request to get Objeto : {}", id);
        return objetoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Objeto : {}", id);
        objetoRepository.deleteById(id);
    }
}
