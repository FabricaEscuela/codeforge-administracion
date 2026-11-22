package com.codefactory.dev_social_network.comentarios.interfaces;

import com.codefactory.dev_social_network.comentarios.domain.CommentReply;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CommentReplyRepositoryPort {
    CommentReply save(CommentReply reply);

    List<CommentReply> findByCommentId(UUID commentId);

    long countByCommentId(UUID commentId);

    /** Total de respuestas por comentario para todos los comentarios de un proyecto. */
    Map<UUID, Long> countByProjectId(UUID projectId);
}