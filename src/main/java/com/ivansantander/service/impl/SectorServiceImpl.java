package com.ivansantander.service.impl;

import com.ivansantander.domain.Sector;
import com.ivansantander.repository.SectorRepository;
import com.ivansantander.service.SectorService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sector}.
 */
@Service
@Transactional
public class SectorServiceImpl implements SectorService {

    private final Logger log = LoggerFactory.getLogger(SectorServiceImpl.class);

    private final SectorRepository sectorRepository;

    public SectorServiceImpl(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    @Override
    public Sector save(Sector sector) {
        log.debug("Request to save Sector : {}", sector);
        return sectorRepository.save(sector);
    }

    @Override
    public Optional<Sector> partialUpdate(Sector sector) {
        log.debug("Request to partially update Sector : {}", sector);

        return sectorRepository
            .findById(sector.getId())
            .map(existingSector -> {
                if (sector.getNombre() != null) {
                    existingSector.setNombre(sector.getNombre());
                }
                if (sector.getValor() != null) {
                    existingSector.setValor(sector.getValor());
                }
                if (sector.getFechaRegistro() != null) {
                    existingSector.setFechaRegistro(sector.getFechaRegistro());
                }

                return existingSector;
            })
            .map(sectorRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sector> findAll(Pageable pageable) {
        log.debug("Request to get all Sectors");
        return sectorRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sector> findOne(Long id) {
        log.debug("Request to get Sector : {}", id);
        return sectorRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sector : {}", id);
        sectorRepository.deleteById(id);
    }
}
