package com.codefactory.dev_social_network.comentarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommentRequest(
        @NotBlank(message = "El comentario no puede estar vacio")
        @Size(max = 1000, message = "Comentario excede el limite de caracteres")
        String comentario
) {
}
