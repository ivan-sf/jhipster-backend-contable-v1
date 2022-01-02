package com.ivansantander.service.impl;

import com.ivansantander.domain.Pais;
import com.ivansantander.repository.PaisRepository;
import com.ivansantander.service.PaisService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pais}.
 */
@Service
@Transactional
public class PaisServiceImpl implements PaisService {

    private final Logger log = LoggerFactory.getLogger(PaisServiceImpl.class);

    private final PaisRepository paisRepository;

    public PaisServiceImpl(PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    @Override
    public Pais save(Pais pais) {
        log.debug("Request to save Pais : {}", pais);
        return paisRepository.save(pais);
    }

    @Override
    public Optional<Pais> partialUpdate(Pais pais) {
        log.debug("Request to partially update Pais : {}", pais);

        return paisRepository
            .findById(pais.getId())
            .map(existingPais -> {
                if (pais.getNombre() != null) {
                    existingPais.setNombre(pais.getNombre());
                }
                if (pais.getCodigoDIAN() != null) {
                    existingPais.setCodigoDIAN(pais.getCodigoDIAN());
                }

                return existingPais;
            })
            .map(paisRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pais> findAll(Pageable pageable) {
        log.debug("Request to get all Pais");
        return paisRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pais> findOne(Long id) {
        log.debug("Request to get Pais : {}", id);
        return paisRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pais : {}", id);
        paisRepository.deleteById(id);
    }
}
