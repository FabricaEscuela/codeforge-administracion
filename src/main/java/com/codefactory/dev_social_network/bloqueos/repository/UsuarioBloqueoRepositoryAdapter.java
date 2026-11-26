package com.codefactory.dev_social_network.bloqueos.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.codefactory.dev_social_network.bloqueos.entity.UsuarioBloqueadoEntity;
import com.codefactory.dev_social_network.bloqueos.interfaces.UsuarioBloqueoRepositoryPort;

@Component
public class UsuarioBloqueoRepositoryAdapter implements UsuarioBloqueoRepositoryPort {

    private final UsuarioBloqueadoJpaRepository jpaRepository;

    public UsuarioBloqueoRepositoryAdapter(UsuarioBloqueadoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean existe(UUID blockerId, UUID blockedId) {
        return jpaRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId);
    }

    @Override
    public void guardar(UUID blockerId, UUID blockedId) {
        jpaRepository.save(new UsuarioBloqueadoEntity(blockerId, blockedId));
    }

    @Override
    public boolean eliminar(UUID blockerId, UUID blockedId) {
        Optional<UsuarioBloqueadoEntity> bloqueo =
                jpaRepository.findByBlockerIdAndBlockedId(blockerId, blockedId);

        if (bloqueo.isEmpty()) {
            return false;
        }

        jpaRepository.delete(bloqueo.get());
        return true;
    }
}
