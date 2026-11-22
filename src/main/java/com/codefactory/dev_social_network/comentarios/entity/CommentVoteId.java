package com.codefactory.dev_social_network.comentarios.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/** Clave compuesta (comentario, usuario): impide votos duplicados. */
public class CommentVoteId implements Serializable {

    private UUID commentId;
    private UUID userId;

    protected CommentVoteId() {
    }

    public CommentVoteId(UUID commentId, UUID userId) {
        this.commentId = commentId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentVoteId other)) return false;
        return Objects.equals(commentId, other.commentId) && Objects.equals(userId, other.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, userId);
    }
}