package com.codefactory.dev_social_network.shared.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class UuidParserTest {

    @Test
    @DisplayName("Devuelve el mismo UUID cuando el texto tiene formato válido")
    void devuelveElUuidValido() {
        // Arrange
        UUID uuid = UUID.randomUUID();

        // Act
        UUID resultado = UuidParser.parseOrNull(uuid.toString());

        // Assert
        assertThat(resultado).isEqualTo(uuid);
    }

    @Test
    @DisplayName("Acepta un UUID válido rodeado de espacios")
    void aceptaUuidConEspacios() {
        // Arrange
        UUID uuid = UUID.randomUUID();

        // Act
        UUID resultado = UuidParser.parseOrNull("  " + uuid + "  ");

        // Assert
        assertThat(resultado).isEqualTo(uuid);
    }

    @Test
    @DisplayName("Devuelve null cuando el texto no es un UUID")
    void devuelveNullCuandoNoEsUuid() {
        // Act
        UUID resultado = UuidParser.parseOrNull("9999");

        // Assert
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Devuelve null cuando el texto tiene un formato inválido")
    void devuelveNullConFormatoInvalido() {
        // Act
        UUID resultado = UuidParser.parseOrNull("no-es-uuid");

        // Assert
        assertThat(resultado).isNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "   " })
    @DisplayName("Devuelve null cuando el texto es nulo, vacío o solo espacios")
    void devuelveNullConTextoVacio(String valor) {
        // Act
        UUID resultado = UuidParser.parseOrNull(valor);

        // Assert
        assertThat(resultado).isNull();
    }
}
