package com.codefactory.dev_social_network.shared.exception;

import org.springframework.http.HttpStatus;

public class ProyectoNoEncontradoException extends BusinessException {
    public ProyectoNoEncontradoException() {
        super(
                "PROYECTO_NO_ENCONTRADO",
                "Project not found",
                HttpStatus.NOT_FOUND
        );
    }
}
