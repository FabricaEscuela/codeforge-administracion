package com.codefactory.dev_social_network.bloqueos.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codefactory.dev_social_network.bloqueos.entity.UsuarioBloqueadoEntity;

public interface UsuarioBloqueadoJpaRepository
        extends JpaRepository<UsuarioBloqueadoEntity, UUID> {

    boolean existsByBlockerIdAndBlockedId(UUID blockerId, UUID blockedId);

    Optional<UsuarioBloqueadoEntity> findByBlockerIdAndBlockedId(UUID blockerId, UUID blockedId);
}
