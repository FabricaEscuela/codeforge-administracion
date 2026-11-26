package com.codefactory.dev_social_network.bloqueos.dto;

public class BloqueoResponse {

    private String mensaje;

    public BloqueoResponse() {
    }

    public BloqueoResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
