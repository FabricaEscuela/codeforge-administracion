package com.codefactory.dev_social_network.proyectos.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "likes_proyecto")
@IdClass(ProjectLikeId.class)
public class ProjectLike {

    @Id
    @Column(name = "id_proyecto", nullable = false)
    private UUID projectId;

    // Referencia por id al módulo usuarios (sin relación JPA entre módulos)
    @Id
    @Column(name = "id_usuario", nullable = false)
    private UUID usuarioId;

    @Column(name = "fecha_creacion", nullable = false)
    private OffsetDateTime createdAt;

    protected ProjectLike() {
        // Requerido por JPA
    }

    public ProjectLike(UUID projectId, UUID usuarioId) {
        this.projectId = projectId;
        this.usuarioId = usuarioId;
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getProjectId() { return projectId; }
    public UUID getUsuarioId() { return usuarioId; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}