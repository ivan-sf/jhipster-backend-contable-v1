package com.ivansantander.service.impl;

import com.ivansantander.domain.Empleado;
import com.ivansantander.repository.EmpleadoRepository;
import com.ivansantander.service.EmpleadoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Empleado}.
 */
@Service
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService {

    private final Logger log = LoggerFactory.getLogger(EmpleadoServiceImpl.class);

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public Empleado save(Empleado empleado) {
        log.debug("Request to save Empleado : {}", empleado);
        return empleadoRepository.save(empleado);
    }

    @Override
    public Optional<Empleado> partialUpdate(Empleado empleado) {
        log.debug("Request to partially update Empleado : {}", empleado);

        return empleadoRepository
            .findById(empleado.getId())
            .map(existingEmpleado -> {
                if (empleado.getNombres() != null) {
                    existingEmpleado.setNombres(empleado.getNombres());
                }
                if (empleado.getApellidos() != null) {
                    existingEmpleado.setApellidos(empleado.getApellidos());
                }
                if (empleado.getTipoPersona() != null) {
                    existingEmpleado.setTipoPersona(empleado.getTipoPersona());
                }
                if (empleado.getTipoDocumento() != null) {
                    existingEmpleado.setTipoDocumento(empleado.getTipoDocumento());
                }
                if (empleado.getDocumento() != null) {
                    existingEmpleado.setDocumento(empleado.getDocumento());
                }
                if (empleado.getDv() != null) {
                    existingEmpleado.setDv(empleado.getDv());
                }
                if (empleado.getEstado() != null) {
                    existingEmpleado.setEstado(empleado.getEstado());
                }

                return existingEmpleado;
            })
            .map(empleadoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Empleado> findAll(Pageable pageable) {
        log.debug("Request to get all Empleados");
        return empleadoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Empleado> findOne(Long id) {
        log.debug("Request to get Empleado : {}", id);
        return empleadoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Empleado : {}", id);
        empleadoRepository.deleteById(id);
    }
}
