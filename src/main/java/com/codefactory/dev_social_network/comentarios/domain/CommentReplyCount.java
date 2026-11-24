package com.codefactory.dev_social_network.comentarios.domain;

import java.util.UUID;

/** Total de respuestas de un comentario; resultado de la consulta agrupada. */
public record CommentReplyCount(UUID commentId, Long total) {
}
