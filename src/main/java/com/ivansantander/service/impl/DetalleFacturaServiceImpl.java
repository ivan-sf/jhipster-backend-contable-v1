package com.ivansantander.service.impl;

import com.ivansantander.domain.DetalleFactura;
import com.ivansantander.repository.DetalleFacturaRepository;
import com.ivansantander.service.DetalleFacturaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DetalleFactura}.
 */
@Service
@Transactional
public class DetalleFacturaServiceImpl implements DetalleFacturaService {

    private final Logger log = LoggerFactory.getLogger(DetalleFacturaServiceImpl.class);

    private final DetalleFacturaRepository detalleFacturaRepository;

    public DetalleFacturaServiceImpl(DetalleFacturaRepository detalleFacturaRepository) {
        this.detalleFacturaRepository = detalleFacturaRepository;
    }

    @Override
    public DetalleFactura save(DetalleFactura detalleFactura) {
        log.debug("Request to save DetalleFactura : {}", detalleFactura);
        return detalleFacturaRepository.save(detalleFactura);
    }

    @Override
    public Optional<DetalleFactura> partialUpdate(DetalleFactura detalleFactura) {
        log.debug("Request to partially update DetalleFactura : {}", detalleFactura);

        return detalleFacturaRepository
            .findById(detalleFactura.getId())
            .map(existingDetalleFactura -> {
                if (detalleFactura.getPrecio() != null) {
                    existingDetalleFactura.setPrecio(detalleFactura.getPrecio());
                }
                if (detalleFactura.getCantidad() != null) {
                    existingDetalleFactura.setCantidad(detalleFactura.getCantidad());
                }
                if (detalleFactura.getTotal() != null) {
                    existingDetalleFactura.setTotal(detalleFactura.getTotal());
                }
                if (detalleFactura.getIva() != null) {
                    existingDetalleFactura.setIva(detalleFactura.getIva());
                }
                if (detalleFactura.getValorImpuesto() != null) {
                    existingDetalleFactura.setValorImpuesto(detalleFactura.getValorImpuesto());
                }
                if (detalleFactura.getEstado() != null) {
                    existingDetalleFactura.setEstado(detalleFactura.getEstado());
                }

                return existingDetalleFactura;
            })
            .map(detalleFacturaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DetalleFactura> findAll(Pageable pageable) {
        log.debug("Request to get all DetalleFacturas");
        return detalleFacturaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleFactura> findOne(Long id) {
        log.debug("Request to get DetalleFactura : {}", id);
        return detalleFacturaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DetalleFactura : {}", id);
        detalleFacturaRepository.deleteById(id);
    }
}
