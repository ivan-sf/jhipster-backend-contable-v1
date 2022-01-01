package com.ivansantander.service.impl;

import com.ivansantander.domain.Factura;
import com.ivansantander.repository.FacturaRepository;
import com.ivansantander.service.FacturaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Factura}.
 */
@Service
@Transactional
public class FacturaServiceImpl implements FacturaService {

    private final Logger log = LoggerFactory.getLogger(FacturaServiceImpl.class);

    private final FacturaRepository facturaRepository;

    public FacturaServiceImpl(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    @Override
    public Factura save(Factura factura) {
        log.debug("Request to save Factura : {}", factura);
        return facturaRepository.save(factura);
    }

    @Override
    public Optional<Factura> partialUpdate(Factura factura) {
        log.debug("Request to partially update Factura : {}", factura);

        return facturaRepository
            .findById(factura.getId())
            .map(existingFactura -> {
                if (factura.getNumero() != null) {
                    existingFactura.setNumero(factura.getNumero());
                }
                if (factura.getCaja() != null) {
                    existingFactura.setCaja(factura.getCaja());
                }
                if (factura.getEstado() != null) {
                    existingFactura.setEstado(factura.getEstado());
                }
                if (factura.getIva19() != null) {
                    existingFactura.setIva19(factura.getIva19());
                }
                if (factura.getBaseIva19() != null) {
                    existingFactura.setBaseIva19(factura.getBaseIva19());
                }
                if (factura.getIva5() != null) {
                    existingFactura.setIva5(factura.getIva5());
                }
                if (factura.getBaseIva5() != null) {
                    existingFactura.setBaseIva5(factura.getBaseIva5());
                }
                if (factura.getIva0() != null) {
                    existingFactura.setIva0(factura.getIva0());
                }
                if (factura.getBaseIva0() != null) {
                    existingFactura.setBaseIva0(factura.getBaseIva0());
                }
                if (factura.getTotal() != null) {
                    existingFactura.setTotal(factura.getTotal());
                }
                if (factura.getPago() != null) {
                    existingFactura.setPago(factura.getPago());
                }
                if (factura.getSaldo() != null) {
                    existingFactura.setSaldo(factura.getSaldo());
                }
                if (factura.getFechaRegistro() != null) {
                    existingFactura.setFechaRegistro(factura.getFechaRegistro());
                }
                if (factura.getFechaActualizacion() != null) {
                    existingFactura.setFechaActualizacion(factura.getFechaActualizacion());
                }

                return existingFactura;
            })
            .map(facturaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Factura> findAll(Pageable pageable) {
        log.debug("Request to get all Facturas");
        return facturaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Factura> findOne(Long id) {
        log.debug("Request to get Factura : {}", id);
        return facturaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Factura : {}", id);
        facturaRepository.deleteById(id);
    }
}
