package com.ivansantander.service.impl;

import com.ivansantander.domain.Cliente;
import com.ivansantander.repository.ClienteRepository;
import com.ivansantander.service.ClienteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cliente}.
 */
@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final Logger log = LoggerFactory.getLogger(ClienteServiceImpl.class);

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente save(Cliente cliente) {
        log.debug("Request to save Cliente : {}", cliente);
        return clienteRepository.save(cliente);
    }

    @Override
    public Optional<Cliente> partialUpdate(Cliente cliente) {
        log.debug("Request to partially update Cliente : {}", cliente);

        return clienteRepository
            .findById(cliente.getId())
            .map(existingCliente -> {
                if (cliente.getNombres() != null) {
                    existingCliente.setNombres(cliente.getNombres());
                }
                if (cliente.getApellidos() != null) {
                    existingCliente.setApellidos(cliente.getApellidos());
                }
                if (cliente.getTipoPersona() != null) {
                    existingCliente.setTipoPersona(cliente.getTipoPersona());
                }
                if (cliente.getTipoDocumento() != null) {
                    existingCliente.setTipoDocumento(cliente.getTipoDocumento());
                }
                if (cliente.getDocumento() != null) {
                    existingCliente.setDocumento(cliente.getDocumento());
                }
                if (cliente.getDv() != null) {
                    existingCliente.setDv(cliente.getDv());
                }
                if (cliente.getEstado() != null) {
                    existingCliente.setEstado(cliente.getEstado());
                }

                return existingCliente;
            })
            .map(clienteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable pageable) {
        log.debug("Request to get all Clientes");
        return clienteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> findOne(Long id) {
        log.debug("Request to get Cliente : {}", id);
        return clienteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cliente : {}", id);
        clienteRepository.deleteById(id);
    }
}
