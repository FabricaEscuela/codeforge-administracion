package com.codefactory.dev_social_network.bloqueos.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/** Bloqueo direccional: el usuario {@code blockerId} bloquea al usuario {@code blockedId}. */
@Entity
@Table(name = "usuarios_bloqueados", uniqueConstraints = @UniqueConstraint(
        name = "uq_bloqueo_par",
        columnNames = {"blocker_id", "blocked_id"}))
public class UsuarioBloqueadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "blocker_id", nullable = false)
    private UUID blockerId;

    @Column(name = "blocked_id", nullable = false)
    private UUID blockedId;

    @Column(name = "fecha_bloqueo", nullable = false)
    private OffsetDateTime fechaBloqueo;

    protected UsuarioBloqueadoEntity() {
    }

    public UsuarioBloqueadoEntity(UUID blockerId, UUID blockedId) {
        this.blockerId = blockerId;
        this.blockedId = blockedId;
    }

    @PrePersist
    protected void antesDeGuardar() {
        if (fechaBloqueo == null) {
            fechaBloqueo = OffsetDateTime.now();
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getBlockerId() {
        return blockerId;
    }

    public UUID getBlockedId() {
        return blockedId;
    }

    public OffsetDateTime getFechaBloqueo() {
        return fechaBloqueo;
    }
}
