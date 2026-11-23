package com.codefactory.dev_social_network.Mensajeria.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public class IniciarConversacionRequest {

    @NotNull(message = "El usuario es obligatorio")
    private UUID usuarioId;

    public IniciarConversacionRequest() {
    }

    public IniciarConversacionRequest(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }
}
