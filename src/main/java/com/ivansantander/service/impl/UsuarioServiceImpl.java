package com.ivansantander.service.impl;

import com.ivansantander.domain.Usuario;
import com.ivansantander.repository.UsuarioRepository;
import com.ivansantander.service.UsuarioService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Usuario}.
 */
@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        log.debug("Request to save Usuario : {}", usuario);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> partialUpdate(Usuario usuario) {
        log.debug("Request to partially update Usuario : {}", usuario);

        return usuarioRepository
            .findById(usuario.getId())
            .map(existingUsuario -> {
                if (usuario.getNombres() != null) {
                    existingUsuario.setNombres(usuario.getNombres());
                }
                if (usuario.getApellidos() != null) {
                    existingUsuario.setApellidos(usuario.getApellidos());
                }
                if (usuario.getTipoPersona() != null) {
                    existingUsuario.setTipoPersona(usuario.getTipoPersona());
                }
                if (usuario.getTipoDocumento() != null) {
                    existingUsuario.setTipoDocumento(usuario.getTipoDocumento());
                }
                if (usuario.getDocumento() != null) {
                    existingUsuario.setDocumento(usuario.getDocumento());
                }
                if (usuario.getDv() != null) {
                    existingUsuario.setDv(usuario.getDv());
                }
                if (usuario.getEstado() != null) {
                    existingUsuario.setEstado(usuario.getEstado());
                }
                if (usuario.getRol() != null) {
                    existingUsuario.setRol(usuario.getRol());
                }
                if (usuario.getRut() != null) {
                    existingUsuario.setRut(usuario.getRut());
                }
                if (usuario.getNombreComercial() != null) {
                    existingUsuario.setNombreComercial(usuario.getNombreComercial());
                }
                if (usuario.getNit() != null) {
                    existingUsuario.setNit(usuario.getNit());
                }
                if (usuario.getDireccion() != null) {
                    existingUsuario.setDireccion(usuario.getDireccion());
                }
                if (usuario.getIndicativo() != null) {
                    existingUsuario.setIndicativo(usuario.getIndicativo());
                }
                if (usuario.getTelefono() != null) {
                    existingUsuario.setTelefono(usuario.getTelefono());
                }
                if (usuario.getEmail() != null) {
                    existingUsuario.setEmail(usuario.getEmail());
                }
                if (usuario.getGenero() != null) {
                    existingUsuario.setGenero(usuario.getGenero());
                }
                if (usuario.getEdad() != null) {
                    existingUsuario.setEdad(usuario.getEdad());
                }
                if (usuario.getFoto() != null) {
                    existingUsuario.setFoto(usuario.getFoto());
                }
                if (usuario.getDescripcion() != null) {
                    existingUsuario.setDescripcion(usuario.getDescripcion());
                }
                if (usuario.getFechaRegistro() != null) {
                    existingUsuario.setFechaRegistro(usuario.getFechaRegistro());
                }

                return existingUsuario;
            })
            .map(usuarioRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> findAll(Pageable pageable) {
        log.debug("Request to get all Usuarios");
        return usuarioRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findOne(Long id) {
        log.debug("Request to get Usuario : {}", id);
        return usuarioRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Usuario : {}", id);
        usuarioRepository.deleteById(id);
    }
}
