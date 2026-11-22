package com.codefactory.dev_social_network.comentarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateReplyRequest(
        @NotBlank(message = "Complete este campo")
        @Size(max = 1000, message = "La respuesta no puede superar 1000 caracteres")
        String respuesta
) {
}
