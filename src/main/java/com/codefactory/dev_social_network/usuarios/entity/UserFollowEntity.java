package com.codefactory.dev_social_network.usuarios.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "seguidores")
@IdClass(UserFollowId.class)
public class UserFollowEntity {

    @Id
    @Column(name = "id_seguidor", nullable = false)
    private UUID followerId;

    @Id
    @Column(name = "id_seguido", nullable = false)
    private UUID followedId;

    @Column(name = "fecha_creacion", nullable = false)
    private OffsetDateTime createdAt;

    protected UserFollowEntity() {
    }

    public UserFollowEntity(UUID followerId, UUID followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getFollowedId() {
        return followedId;
    }
}