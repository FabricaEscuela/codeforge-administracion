package com.codefactory.dev_social_network.bloqueos.interfaces;

import java.util.UUID;

/**
 * Puerto de persistencia de bloqueos entre usuarios. No expone entidades JPA:
 * solo operaciones de dominio sobre el par (blocker, blocked).
 */
public interface UsuarioBloqueoRepositoryPort {

    boolean existe(UUID blockerId, UUID blockedId);

    void guardar(UUID blockerId, UUID blockedId);

    boolean eliminar(UUID blockerId, UUID blockedId);
}
