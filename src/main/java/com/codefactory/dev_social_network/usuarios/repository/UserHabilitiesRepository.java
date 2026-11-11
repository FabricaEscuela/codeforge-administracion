package com.codefactory.dev_social_network.usuarios.repository;

import com.codefactory.dev_social_network.usuarios.entity.UserHabilitiesEntity;
import com.codefactory.dev_social_network.usuarios.entity.UserHabilitiesId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserHabilitiesRepository extends Repository<UserHabilitiesEntity, UserHabilitiesId> {

    UserHabilitiesEntity save(UserHabilitiesEntity userHabilitiesEntity);

    @Query("select t.name from UserHabilitiesEntity h, "
            + "com.codefactory.dev_social_network.proyectos.entity.Technology t "
            + "where t.id = h.technologyId and h.profileId = :profileId order by t.name")
    List<String> findSkillNamesByProfileId(@Param("profileId") UUID profileId);

    // Borrado directo en la BD: así volver a insertar la misma habilidad no choca con la clave primaria
    @Modifying
    @Query("delete from UserHabilitiesEntity h where h.profileId = :profileId")
    void deleteByProfileId(@Param("profileId") UUID profileId);
}