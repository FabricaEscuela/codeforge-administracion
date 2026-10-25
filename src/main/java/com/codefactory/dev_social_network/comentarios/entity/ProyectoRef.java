package com.codefactory.dev_social_network.comentarios.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

/**
 * Referencia de solo lectura a la tabla real "proyectos" (id UUID).
 *
 * El modulo "proyectos" de este servicio todavia mapea la tabla "projects"
 * con id Long, que no coincide con schema.sql. Para comentar solo hace
 * falta saber si el proyecto existe, asi que se mapea unicamente el id.
 * Sin constructor publico ni setters: este modulo nunca crea ni modifica
 * proyectos.
 */
@Entity
@Table(name = "proyectos")
public class ProyectoRef {

    @Id
    private UUID id;

    protected ProyectoRef() {
    }

    public UUID getId() {
        return id;
    }
}
