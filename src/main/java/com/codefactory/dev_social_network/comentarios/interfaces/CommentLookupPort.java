package com.codefactory.dev_social_network.comentarios.interfaces;

import java.util.Optional;
import java.util.UUID;

/** Permite saber a que proyecto pertenece un comentario, sin exponer su entidad. */
public interface CommentLookupPort {
    Optional<UUID> findProjectIdByCommentId(UUID commentId);
}