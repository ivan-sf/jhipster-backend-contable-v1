package com.ivansantander.service.impl;

import com.ivansantander.domain.Lote;
import com.ivansantander.repository.LoteRepository;
import com.ivansantander.service.LoteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Lote}.
 */
@Service
@Transactional
public class LoteServiceImpl implements LoteService {

    private final Logger log = LoggerFactory.getLogger(LoteServiceImpl.class);

    private final LoteRepository loteRepository;

    public LoteServiceImpl(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    @Override
    public Lote save(Lote lote) {
        log.debug("Request to save Lote : {}", lote);
        return loteRepository.save(lote);
    }

    @Override
    public Optional<Lote> partialUpdate(Lote lote) {
        log.debug("Request to partially update Lote : {}", lote);

        return loteRepository
            .findById(lote.getId())
            .map(existingLote -> {
                if (lote.getNumero() != null) {
                    existingLote.setNumero(lote.getNumero());
                }

                return existingLote;
            })
            .map(loteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Lote> findAll(Pageable pageable) {
        log.debug("Request to get all Lotes");
        return loteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Lote> findOne(Long id) {
        log.debug("Request to get Lote : {}", id);
        return loteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Lote : {}", id);
        loteRepository.deleteById(id);
    }
}
