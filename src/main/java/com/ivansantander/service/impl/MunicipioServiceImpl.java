package com.ivansantander.service.impl;

import com.ivansantander.domain.Municipio;
import com.ivansantander.repository.MunicipioRepository;
import com.ivansantander.service.MunicipioService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Municipio}.
 */
@Service
@Transactional
public class MunicipioServiceImpl implements MunicipioService {

    private final Logger log = LoggerFactory.getLogger(MunicipioServiceImpl.class);

    private final MunicipioRepository municipioRepository;

    public MunicipioServiceImpl(MunicipioRepository municipioRepository) {
        this.municipioRepository = municipioRepository;
    }

    @Override
    public Municipio save(Municipio municipio) {
        log.debug("Request to save Municipio : {}", municipio);
        return municipioRepository.save(municipio);
    }

    @Override
    public Optional<Municipio> partialUpdate(Municipio municipio) {
        log.debug("Request to partially update Municipio : {}", municipio);

        return municipioRepository
            .findById(municipio.getId())
            .map(existingMunicipio -> {
                if (municipio.getNombre() != null) {
                    existingMunicipio.setNombre(municipio.getNombre());
                }
                if (municipio.getCodigoDIAN() != null) {
                    existingMunicipio.setCodigoDIAN(municipio.getCodigoDIAN());
                }

                return existingMunicipio;
            })
            .map(municipioRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Municipio> findAll(Pageable pageable) {
        log.debug("Request to get all Municipios");
        return municipioRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Municipio> findOne(Long id) {
        log.debug("Request to get Municipio : {}", id);
        return municipioRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Municipio : {}", id);
        municipioRepository.deleteById(id);
    }
}
