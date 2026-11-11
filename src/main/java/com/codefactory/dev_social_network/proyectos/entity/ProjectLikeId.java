package com.codefactory.dev_social_network.proyectos.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/** Clave compuesta (proyecto, usuario): impide dar dos likes al mismo proyecto. */
public class ProjectLikeId implements Serializable {

    private UUID projectId;
    private UUID usuarioId;

    protected ProjectLikeId() {
    }

    public ProjectLikeId(UUID projectId, UUID usuarioId) {
        this.projectId = projectId;
        this.usuarioId = usuarioId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectLikeId other)) return false;
        return Objects.equals(projectId, other.projectId) && Objects.equals(usuarioId, other.usuarioId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, usuarioId);
    }
}