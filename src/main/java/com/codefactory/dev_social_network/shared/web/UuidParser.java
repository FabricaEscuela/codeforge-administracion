package com.codefactory.dev_social_network.shared.web;

import java.util.UUID;

/**
 * Traduce el valor textual recibido en la capa HTTP a un UUID de dominio.
 * Un valor ausente o con formato inválido se representa como {@code null},
 * dejando que el caso de uso decida el error de negocio aplicable.
 */
public final class UuidParser {

    private UuidParser() {
    }

    public static UUID parseOrNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(value.trim());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
