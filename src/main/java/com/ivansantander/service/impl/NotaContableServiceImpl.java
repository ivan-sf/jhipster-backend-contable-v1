package com.ivansantander.service.impl;

import com.ivansantander.domain.NotaContable;
import com.ivansantander.repository.NotaContableRepository;
import com.ivansantander.service.NotaContableService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NotaContable}.
 */
@Service
@Transactional
public class NotaContableServiceImpl implements NotaContableService {

    private final Logger log = LoggerFactory.getLogger(NotaContableServiceImpl.class);

    private final NotaContableRepository notaContableRepository;

    public NotaContableServiceImpl(NotaContableRepository notaContableRepository) {
        this.notaContableRepository = notaContableRepository;
    }

    @Override
    public NotaContable save(NotaContable notaContable) {
        log.debug("Request to save NotaContable : {}", notaContable);
        return notaContableRepository.save(notaContable);
    }

    @Override
    public Optional<NotaContable> partialUpdate(NotaContable notaContable) {
        log.debug("Request to partially update NotaContable : {}", notaContable);

        return notaContableRepository
            .findById(notaContable.getId())
            .map(existingNotaContable -> {
                if (notaContable.getNumero() != null) {
                    existingNotaContable.setNumero(notaContable.getNumero());
                }

                return existingNotaContable;
            })
            .map(notaContableRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotaContable> findAll(Pageable pageable) {
        log.debug("Request to get all NotaContables");
        return notaContableRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotaContable> findOne(Long id) {
        log.debug("Request to get NotaContable : {}", id);
        return notaContableRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete NotaContable : {}", id);
        notaContableRepository.deleteById(id);
    }
}
