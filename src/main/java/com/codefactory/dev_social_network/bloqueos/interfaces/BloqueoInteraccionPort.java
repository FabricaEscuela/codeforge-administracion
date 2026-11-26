package com.codefactory.dev_social_network.bloqueos.interfaces;

import java.util.UUID;

/**
 * Puerto de dominio que responde si la interacción entre dos usuarios está
 * restringida por un bloqueo.
 */
public interface BloqueoInteraccionPort {

    boolean estaBloqueada(UUID usuarioA, UUID usuarioB);
}
