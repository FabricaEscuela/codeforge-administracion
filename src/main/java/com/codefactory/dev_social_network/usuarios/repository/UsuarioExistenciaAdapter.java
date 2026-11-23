package com.codefactory.dev_social_network.usuarios.repository;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.codefactory.dev_social_network.usuarios.interfaces.UsuarioExistenciaPort;

@Component
public class UsuarioExistenciaAdapter implements UsuarioExistenciaPort {

    private final UsuarioJpaRepository jpaRepository;

    public UsuarioExistenciaAdapter(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean existe(UUID usuarioId) {
        return usuarioId != null && jpaRepository.existsById(usuarioId);
    }
}
