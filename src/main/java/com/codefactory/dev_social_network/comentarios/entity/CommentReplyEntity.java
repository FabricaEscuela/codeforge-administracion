package com.codefactory.dev_social_network.comentarios.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Mapea la tabla respuestas_comentario de schema.sql (HU014). */
@Entity
@Table(name = "respuestas_comentario")
public class CommentReplyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "id_comentario", nullable = false)
    private UUID commentId;

    @Column(name = "id_usuario", nullable = false)
    private UUID userId;

    @Column(name = "respuesta", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "fecha_creacion", nullable = false)
    private OffsetDateTime createdAt;

    protected CommentReplyEntity() {
    }

    public CommentReplyEntity(UUID commentId, UUID userId, String content) {
        this.commentId = commentId;
        this.userId = userId;
        this.content = content;
    }

    @PrePersist
    protected void assignCreationTime() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getCommentId() {
        return commentId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}