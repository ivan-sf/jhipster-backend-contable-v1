package com.ivansantander.service.impl;

import com.ivansantander.domain.Empresa;
import com.ivansantander.repository.EmpresaRepository;
import com.ivansantander.service.EmpresaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Empresa}.
 */
@Service
@Transactional
public class EmpresaServiceImpl implements EmpresaService {

    private final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);

    private final EmpresaRepository empresaRepository;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Override
    public Empresa save(Empresa empresa) {
        log.debug("Request to save Empresa : {}", empresa);
        return empresaRepository.save(empresa);
    }

    @Override
    public Optional<Empresa> partialUpdate(Empresa empresa) {
        log.debug("Request to partially update Empresa : {}", empresa);

        return empresaRepository
            .findById(empresa.getId())
            .map(existingEmpresa -> {
                if (empresa.getRazonSocial() != null) {
                    existingEmpresa.setRazonSocial(empresa.getRazonSocial());
                }
                if (empresa.getNombreComercial() != null) {
                    existingEmpresa.setNombreComercial(empresa.getNombreComercial());
                }
                if (empresa.getTipoDocumento() != null) {
                    existingEmpresa.setTipoDocumento(empresa.getTipoDocumento());
                }
                if (empresa.getDocumento() != null) {
                    existingEmpresa.setDocumento(empresa.getDocumento());
                }
                if (empresa.getDv() != null) {
                    existingEmpresa.setDv(empresa.getDv());
                }
                if (empresa.getDireccion() != null) {
                    existingEmpresa.setDireccion(empresa.getDireccion());
                }
                if (empresa.getIndicativo() != null) {
                    existingEmpresa.setIndicativo(empresa.getIndicativo());
                }
                if (empresa.getTelefono() != null) {
                    existingEmpresa.setTelefono(empresa.getTelefono());
                }
                if (empresa.getEmail() != null) {
                    existingEmpresa.setEmail(empresa.getEmail());
                }
                if (empresa.getLogo() != null) {
                    existingEmpresa.setLogo(empresa.getLogo());
                }
                if (empresa.getResolucionFacturas() != null) {
                    existingEmpresa.setResolucionFacturas(empresa.getResolucionFacturas());
                }
                if (empresa.getPrefijoInicial() != null) {
                    existingEmpresa.setPrefijoInicial(empresa.getPrefijoInicial());
                }
                if (empresa.getPrefijoFinal() != null) {
                    existingEmpresa.setPrefijoFinal(empresa.getPrefijoFinal());
                }
                if (empresa.getPrefijoActual() != null) {
                    existingEmpresa.setPrefijoActual(empresa.getPrefijoActual());
                }
                if (empresa.getDescripcion() != null) {
                    existingEmpresa.setDescripcion(empresa.getDescripcion());
                }
                if (empresa.getFechaRegistro() != null) {
                    existingEmpresa.setFechaRegistro(empresa.getFechaRegistro());
                }
                if (empresa.getEstado() != null) {
                    existingEmpresa.setEstado(empresa.getEstado());
                }

                return existingEmpresa;
            })
            .map(empresaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Empresa> findAll(Pageable pageable) {
        log.debug("Request to get all Empresas");
        return empresaRepository.findAll(pageable);
    }

    public Page<Empresa> findAllWithEagerRelationships(Pageable pageable) {
        return empresaRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Empresa> findOne(Long id) {
        log.debug("Request to get Empresa : {}", id);
        return empresaRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Empresa : {}", id);
        empresaRepository.deleteById(id);
    }
}
