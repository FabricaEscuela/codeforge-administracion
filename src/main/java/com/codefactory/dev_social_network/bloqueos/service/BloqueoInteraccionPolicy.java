package com.codefactory.dev_social_network.bloqueos.service;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.codefactory.dev_social_network.bloqueos.interfaces.BloqueoInteraccionPort;
import com.codefactory.dev_social_network.bloqueos.interfaces.UsuarioBloqueoRepositoryPort;

/**
 * Política reutilizable de interacción: existe bloqueo si cualquiera de los dos
 * usuarios ha bloqueado al otro (el bloqueo es direccional, la restricción es mutua).
 */
@Component
public class BloqueoInteraccionPolicy implements BloqueoInteraccionPort {

    private final UsuarioBloqueoRepositoryPort bloqueoRepository;

    public BloqueoInteraccionPolicy(UsuarioBloqueoRepositoryPort bloqueoRepository) {
        this.bloqueoRepository = bloqueoRepository;
    }

    @Override
    public boolean estaBloqueada(UUID usuarioA, UUID usuarioB) {
        if (usuarioA == null || usuarioB == null || usuarioA.equals(usuarioB)) {
            return false;
        }

        return bloqueoRepository.existe(usuarioA, usuarioB)
                || bloqueoRepository.existe(usuarioB, usuarioA);
    }
}
