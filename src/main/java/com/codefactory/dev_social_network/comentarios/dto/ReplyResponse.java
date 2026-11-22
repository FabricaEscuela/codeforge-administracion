package com.codefactory.dev_social_network.comentarios.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ReplyResponse(
        UUID id,
        UUID idComentario,
        UUID idUsuario,
        String username,
        String respuesta,
        OffsetDateTime fechaCreacion
) {
}