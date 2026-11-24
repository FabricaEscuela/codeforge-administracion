package com.codefactory.dev_social_network.comentarios.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Respuesta a un comentario de proyecto (HU014). Modelo de dominio sin dependencias de framework. */
public record CommentReply(
        UUID id,
        UUID commentId,
        UUID userId,
        String content,
        OffsetDateTime createdAt
) {
}