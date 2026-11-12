package com.codefactory.dev_social_network.comentarios.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "votos_comentario")
@IdClass(CommentVoteId.class)
public class CommentVoteEntity {

    @Id
    @Column(name = "id_comentario", nullable = false)
    private UUID commentId;

    @Id
    @Column(name = "id_usuario", nullable = false)
    private UUID userId;

    @Column(name = "fecha_creacion", nullable = false)
    private OffsetDateTime createdAt;

    protected CommentVoteEntity() {
    }

    public CommentVoteEntity(UUID commentId, UUID userId) {
        this.commentId = commentId;
        this.userId = userId;
    }

    @PrePersist
    protected void assignCreationTime() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

    public UUID getCommentId() {
        return commentId;
    }

    public UUID getUserId() {
        return userId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}