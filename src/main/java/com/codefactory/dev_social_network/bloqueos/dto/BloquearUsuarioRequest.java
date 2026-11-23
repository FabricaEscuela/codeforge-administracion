package com.codefactory.dev_social_network.bloqueos.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public class BloquearUsuarioRequest {

    @NotNull(message = "El usuario es obligatorio")
    private UUID usuarioId;

    public BloquearUsuarioRequest() {
    }

    public BloquearUsuarioRequest(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }
}
