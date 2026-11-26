package com.codefactory.dev_social_network.Mensajeria.dto;

public class IniciarConversacionRequest {

    private String usuarioId;

    public IniciarConversacionRequest() {
    }

    public IniciarConversacionRequest(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
}
