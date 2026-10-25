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

@Entity
@Table(name = "interaccion_proyecto")
public class ProjectCommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "id_proyecto", nullable = false)
    private UUID projectId;

    @Column(name = "id_usuario", nullable = false)
    private UUID userId;

    @Column(name = "comentario", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "fecha_creacion", nullable = false)
    private OffsetDateTime createdAt;

    protected ProjectCommentEntity() {
    }

    public ProjectCommentEntity(UUID projectId, UUID userId, String content) {
        this.projectId = projectId;
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

    public UUID getProjectId() {
        return projectId;
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
