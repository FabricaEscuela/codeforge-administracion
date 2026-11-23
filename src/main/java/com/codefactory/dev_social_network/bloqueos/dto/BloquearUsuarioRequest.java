package com.codefactory.dev_social_network.bloqueos.dto;

public class BloquearUsuarioRequest {

    private String usuarioId;

    public BloquearUsuarioRequest() {
    }

    public BloquearUsuarioRequest(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
}
