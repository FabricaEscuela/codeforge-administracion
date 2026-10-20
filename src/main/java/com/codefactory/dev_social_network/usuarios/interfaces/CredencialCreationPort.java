package com.codefactory.dev_social_network.usuarios.interfaces;

import java.util.UUID;

public interface CredencialCreationPort {
    void crearCredencialLocal(UUID usuarioId, String passwordHash);
}