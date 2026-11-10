package com.codefactory.dev_social_network.usuarios.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/** Clave compuesta (perfil, tecnología) de la tabla habilidades. */
public class UserHabilitiesId implements Serializable {

    private UUID profileId;
    private UUID technologyId;

    protected UserHabilitiesId() {
    }

    public UserHabilitiesId(UUID profileId, UUID technologyId) {
        this.profileId = profileId;
        this.technologyId = technologyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserHabilitiesId other)) return false;
        return Objects.equals(profileId, other.profileId) && Objects.equals(technologyId, other.technologyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId, technologyId);
    }
}