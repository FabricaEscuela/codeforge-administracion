package com.codefactory.dev_social_network.usuarios.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/** Clave compuesta (seguidor, seguido): impide seguir dos veces al mismo usuario. */
public class UserFollowId implements Serializable {

    private UUID followerId;
    private UUID followedId;

    protected UserFollowId() {
    }

    public UserFollowId(UUID followerId, UUID followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserFollowId other)) return false;
        return Objects.equals(followerId, other.followerId) && Objects.equals(followedId, other.followedId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followerId, followedId);
    }
}