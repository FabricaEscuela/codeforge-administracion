package com.codefactory.dev_social_network.usuarios.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

/** Habilidad de un perfil: relaciona perfiles_usuario con tecnologias. */
@Entity
@Table(name = "habilidades")
@IdClass(UserHabilitiesId.class)
public class UserHabilitiesEntity {

    @Id
    @Column(name = "id_perfil", nullable = false)
    private UUID profileId;

    @Id
    @Column(name = "id_tecnologia", nullable = false)
    private UUID technologyId;

    protected UserHabilitiesEntity() {}

    public UserHabilitiesEntity(UUID profileId, UUID technologyId) {
        this.profileId = profileId;
        this.technologyId = technologyId;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public UUID getTechnologyId() {
        return technologyId;
    }
}